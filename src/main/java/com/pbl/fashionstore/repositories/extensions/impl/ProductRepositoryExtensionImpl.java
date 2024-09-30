package com.pbl.fashionstore.repositories.extensions.impl;

import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.enums.DiscountType;
import com.pbl.fashionstore.enums.SortOption;
import com.pbl.fashionstore.repositories.extensions.ProductRepositoryExtension;
import com.pbl.fashionstore.utils.DiscountCalculator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

public class ProductRepositoryExtensionImpl implements ProductRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductOverviewDTO> findProductsByFilters(ProductFilterCriteriaParams filterParams) {

        String sql = "select p.product_id, p.name, p.thumbnail_url, p.price, p.created_at, d.discount_value, d.discount_type, " +
                "(select avg(rating) from product_review pr where pr.product_id = p.product_id) as rating " +
                "from product p " +
                "left join " +
                "(select pd0.product_id, d0.discount_value, d0.discount_type " +
                "from product_discount pd0 " +
                "inner join discount_offer d0 " +
                "on pd0.discount_offer_id = d0.discount_offer_id " +
                "where d0.start_date < :now and d0.end_date > :now) " +
                "as d " +
                "on p.product_id = d.product_id " +
                "where p.category_id = :categoryId ";

        sql = sql + constructConditionStatement(filterParams) + constructOrderStatement(filterParams);

        sql = sql + " fetch first :limit rows only";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("now", Instant.now());
        setFilterParameters(query, filterParams);
        query.setParameter("limit", filterParams.getLimit());

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> {
                            BigDecimal priceSale = (BigDecimal) row[3];
                            if (row[6] != null) {
                                priceSale = DiscountCalculator.calculatePriceSale((BigDecimal) row[3], (BigDecimal) row[5], DiscountType.valueOf((String) row[6]));
                            }

                            BigDecimal rating = null;
                            if (row[7] != null) {
                                rating = ((BigDecimal) row[7]).setScale(1, RoundingMode.HALF_UP);
                            }

                            return ProductOverviewDTO.builder()
                                    .id((Long) row[0])
                                    .name((String) row[1])
                                    .thumbnailUrl((String) row[2])
                                    .price((BigDecimal) row[3])
                                    .createdAt((Instant) row[4])
                                    .priceSale(priceSale)
                                    .rating(rating)
                                    .build();
                        }
                )
                .toList();
    }

    @Override
    public Long countProductsByFilters(ProductFilterCriteriaParams filterParams) {
        String sql = "select count(*) from product p where p.category_id = :categoryId " + constructConditionStatement(filterParams);

        Query query = entityManager.createNativeQuery(sql, Long.class);
        setFilterParameters(query, filterParams);

        return (Long) query.getSingleResult();
    }

    public boolean isLastByFilters(ProductFilterCriteriaParams filterParams) {
        return countProductsByFilters(filterParams) == 0;
    }

    private String constructConditionStatement(ProductFilterCriteriaParams filterParams) {
        String condition = "";

        if (filterParams.getSizeIds() != null && !filterParams.getSizeIds().isEmpty()
                && filterParams.getColorIds() != null && !filterParams.getColorIds().isEmpty()) {
            condition = condition + "and p.product_id in (select product_id from product_variant where color_id in :colorIds and size_id in :sizeIds) ";
        } else if (filterParams.getColorIds() != null && !filterParams.getColorIds().isEmpty()) {
            condition = condition + "and p.product_id in (select product_id from product_variant where color_id in :colorIds) ";
        } else if (filterParams.getSizeIds() != null && !filterParams.getSizeIds().isEmpty()) {
            condition = condition + "and p.product_id in (select product_id from product_variant where size_id in :sizeIds) ";
        }

        if (filterParams.getMinPrice() != null && filterParams.getMaxPrice() != null) {
            condition = condition + "and p.price between :minPrice and :maxPrice ";
        } else if (filterParams.getMinPrice() != null) {
            condition = condition + "and p.price >= :minPrice ";
        }

        if (filterParams.getStyleIds() != null && !filterParams.getStyleIds().isEmpty()) {
            condition = condition + "and p.product_id in (select product_id from style_product where style_id in :styleIds) ";
        }

        if (filterParams.getSortOption() == null) {
            if (filterParams.getCursorId() != null) {
                condition = condition + "and p.product_id < :cursorId ";
            }
        } else if (filterParams.getSortOption().equals(SortOption.NEWEST)) {
            if (filterParams.getCursorValue() != null) {
                condition = condition + "and (p.created_at < :cursorValue or (p.created_at = :cursorValue and p.product_id < :cursorId)) ";
            }
        } else if (filterParams.getSortOption().equals(SortOption.RATING)) {
            if (filterParams.getCursorValue() != null) {
                condition = condition + "and (rating < :cursorValue or (rating = :cursorValue and p.product_id < :cursorId)) ";
            }
        }

        return condition;
    }

    private String constructOrderStatement(ProductFilterCriteriaParams filterParams) {
        String order = "";
        if (filterParams.getSortOption() == null) {
            order = "order by p.product_id desc ";
        } else if (filterParams.getSortOption().equals(SortOption.NEWEST)) {
            order = "order by p.created_at desc ";
        } else if (filterParams.getSortOption().equals(SortOption.RATING)) {
            order = "order by rating desc ";
        }
        return order;
    }

    private void setFilterParameters(Query query, ProductFilterCriteriaParams filterParams) {

        query.setParameter("categoryId", filterParams.getCategoryId());

        if (filterParams.getSizeIds() != null && !filterParams.getSizeIds().isEmpty()) {
            query.setParameter("sizeIds", filterParams.getSizeIds());
        }

        if (filterParams.getColorIds() != null && !filterParams.getColorIds().isEmpty()) {
            query.setParameter("colorIds", filterParams.getColorIds());
        }

        if (filterParams.getMinPrice() != null) {
            query.setParameter("minPrice", filterParams.getMinPrice());
        }

        if (filterParams.getMaxPrice() != null) {
            query.setParameter("maxPrice", filterParams.getMaxPrice());
        }

        if (filterParams.getStyleIds() != null && !filterParams.getStyleIds().isEmpty()) {
            query.setParameter("styleIds", filterParams.getStyleIds());
        }

        if (filterParams.getCursorId() != null) {
            query.setParameter("cursorId", filterParams.getCursorId());
        }

        if (filterParams.getCursorValue() != null) {
            query.setParameter("cursorValue", filterParams.getCursorValue());
        }
    }
}