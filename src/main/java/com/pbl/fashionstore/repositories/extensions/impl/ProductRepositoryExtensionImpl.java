package com.pbl.fashionstore.repositories.extensions.impl;

import com.pbl.fashionstore.dtos.dto.*;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.enums.DiscountType;
import com.pbl.fashionstore.enums.SortOption;
import com.pbl.fashionstore.repositories.extensions.ProductRepositoryExtension;
import com.pbl.fashionstore.utils.DiscountCalculator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
                "AVG(pr.rating) as rating " +
                "from product p " +
                "left join product_review pr on p.product_id = pr.product_id " +
                "left join " +
                "(select pd0.product_id, d0.discount_value, d0.discount_type " +
                "from product_discount pd0 " +
                "inner join discount_offer d0 " +
                "on pd0.discount_offer_id = d0.discount_offer_id " +
                "where d0.start_date < :now and d0.end_date > :now) " +
                "as d " +
                "on p.product_id = d.product_id ";

        ConditionStatement conditionStatement = constructConditionStatement(filterParams);

        sql = sql + conditionStatement.getWhereStatement();
        sql = sql + " group by p.product_id, p.name, p.thumbnail_url, p.price, p.created_at, d.discount_value, d.discount_type ";
        sql = sql + conditionStatement.getHavingStatement();
        sql = sql + constructOrderStatement(filterParams);
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
        String sql = "select count(*) from product p ";

        ConditionStatement conditionStatement = constructConditionStatement(filterParams);

        if (!conditionStatement.getHavingStatement().equals("having true ")) {
            sql = sql + "left join product_review pr on p.product_id = pr.product_id " +
                    conditionStatement.getWhereStatement() +
                    " group by p.product_id " +
                    conditionStatement.getHavingStatement();

            Query query = entityManager.createNativeQuery(sql, Long.class);
            setFilterParameters(query, filterParams);

            return (long) query.getResultList().size();
        } else {
            sql = sql + conditionStatement.getWhereStatement();

            Query query = entityManager.createNativeQuery(sql, Long.class);
            setFilterParameters(query, filterParams);

            return (Long) query.getSingleResult();
        }
    }

    public boolean isLastByFilters(ProductFilterCriteriaParams filterParams) {
        return countProductsByFilters(filterParams) == 0;
    }

    private ConditionStatement constructConditionStatement(ProductFilterCriteriaParams filterParams) {
        String whereStatement = "where true ";
        String havingStatement = "having true ";

        if (filterParams.getCategoryId() != null) {
            whereStatement = whereStatement + "and p.category_id = :categoryId ";
        }

        if (filterParams.getSizeIds() != null && !filterParams.getSizeIds().isEmpty()
                && filterParams.getColorIds() != null && !filterParams.getColorIds().isEmpty()) {
            whereStatement = whereStatement + "and p.product_id in (select product_id from product_variant where color_id in :colorIds and size_id in :sizeIds) ";
        } else if (filterParams.getColorIds() != null && !filterParams.getColorIds().isEmpty()) {
            whereStatement = whereStatement + "and p.product_id in (select product_id from product_variant where color_id in :colorIds) ";
        } else if (filterParams.getSizeIds() != null && !filterParams.getSizeIds().isEmpty()) {
            whereStatement = whereStatement + "and p.product_id in (select product_id from product_variant where size_id in :sizeIds) ";
        }

        if (filterParams.getMinPrice() != null && filterParams.getMaxPrice() != null) {
            whereStatement = whereStatement + "and p.price between :minPrice and :maxPrice ";
        } else if (filterParams.getMinPrice() != null) {
            whereStatement = whereStatement + "and p.price >= :minPrice ";
        } else if (filterParams.getMaxPrice() != null) {
            whereStatement = whereStatement + "and p.price <= :maxPrice ";
        }

        if (filterParams.getStyleIds() != null && !filterParams.getStyleIds().isEmpty()) {
            whereStatement = whereStatement + "and p.product_id in (select product_id from style_product where style_id in :styleIds) ";
        }

        if (filterParams.getSortOption() == null) {
            if (filterParams.getCursorId() != null) {
                whereStatement = whereStatement + "and p.product_id < :cursorId ";
            }
        } else if (filterParams.getSortOption().equals(SortOption.NEWEST)) {
            if (filterParams.getCursorValue() != null) {
                whereStatement = whereStatement + "and (p.created_at < :cursorValue or (p.created_at = :cursorValue and p.product_id < :cursorId)) ";
            }
        } else if (filterParams.getSortOption().equals(SortOption.RATING)) {
            if (filterParams.getCursorValue() != null) {
                havingStatement = havingStatement + "and (AVG(rating) < :cursorValue or (AVG(rating) = :cursorValue and p.product_id < :cursorId) or AVG(rating) is null) ";
            } else if (filterParams.getCursorId() != null) {
                havingStatement = havingStatement + "and (AVG(rating) is null and p.product_id < :cursorId) ";
            }
        }

        return new ConditionStatement(whereStatement, havingStatement);
    }

    private String constructOrderStatement(ProductFilterCriteriaParams filterParams) {
        String order = "";
        if (filterParams.getSortOption() == null) {
            order = "order by p.product_id desc ";
        } else if (filterParams.getSortOption().equals(SortOption.NEWEST)) {
            order = "order by p.created_at desc, p.product_id desc ";
        } else if (filterParams.getSortOption().equals(SortOption.RATING)) {
            order = "order by rating desc NULLS LAST, p.product_id desc ";
        }
        return order;
    }

    private void setFilterParameters(Query query, ProductFilterCriteriaParams filterParams) {

        if (filterParams.getCategoryId() != null) {
            query.setParameter("categoryId", filterParams.getCategoryId());
        }

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
            if (filterParams.getSortOption() != null) {
                if (filterParams.getSortOption().equals(SortOption.NEWEST)) {
                    Instant instant = Instant.parse((String) filterParams.getCursorValue());
                    query.setParameter("cursorValue", instant);
                } else if (filterParams.getSortOption().equals(SortOption.RATING)) {
                    BigDecimal rating = new BigDecimal(String.valueOf(filterParams.getCursorValue()));
                    query.setParameter("cursorValue", rating);
                }
            }
        }
    }

    @Override
    public ProductDetailsDTO findProductDetailsById(Long id) {

        String sql = "SELECT p.product_id, p.name, p.description, p.price, avg(pr.rating), count(pr), d.discount_type, d.discount_value, d.end_date " +
                "FROM product p " +
                "LEFT JOIN product_review pr " +
                "ON p.product_id = pr.product_id " +
                "LEFT JOIN " +
                "(SELECT pd0.product_id, d0.discount_value, d0.discount_type, d0.end_date " +
                "from product_discount pd0  " +
                "INNER JOIN discount_offer d0 " +
                "on pd0.discount_offer_id = d0.discount_offer_id " +
                "where d0.start_date < :now and d0.end_date > :now) as d " +
                "ON p.product_id = d.product_id " +
                "WHERE p.product_id = :productId " +
                "GROUP BY p.product_id, d.discount_type, d.discount_value, d.end_date " +
                "FETCH FIRST 1 ROWS ONLY";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("now", Instant.now());
        query.setParameter("productId", id);

        Object[] result;
        try {
            result = (Object[]) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        BigDecimal priceSale = (BigDecimal) result[3];
        if (result[6] != null) {
            priceSale = DiscountCalculator.calculatePriceSale((BigDecimal) result[3], (BigDecimal) result[7], DiscountType.valueOf((String) result[6]));
        }

        return ProductDetailsDTO.builder()
                .id((Long) result[0])
                .name((String) result[1])
                .description((String) result[2])
                .price((BigDecimal) result[3])
                .priceSale(priceSale)
                .rating(result[4] != null ? ((BigDecimal) result[4]).setScale(1, RoundingMode.HALF_UP) : null)
                .reviewCount(((Long) result[5]))
                .discountExpiration((Instant) result[8])
                .build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static
    class ConditionStatement {
        private String whereStatement;
        private String havingStatement;
    }
}


