package com.pbl.fashionstore.repositories.extensions.impl;

import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;
import com.pbl.fashionstore.repositories.extensions.ProductRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.Instant;
import java.util.List;

public class ProductRepositoryExtensionImpl implements ProductRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductOverviewDTO> getProductOverviewIn(List<Long> productIds) {
        String sql = "select p.product_id, name, price, thumbnail_url, discount_value, discount_type, " +
                "(select avg(rating) from product_review pr where pr.product_id = p.product_id) as rating " +
                "from product p " +
                "left join (select * from discount_offer where start_date < :now and end_date > :now) as discount " +
                "on p.product_id = discount.product_id " +
                "where p.product_id in :productIds";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("now", Instant.now());
        query.setParameter("productIds", productIds);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> {
                    Double price = (Double) row[2];
                    Double priceSale = price;
                    if (row[4] != null && row[5] != null) {
                        if (row[5].equals("PERCENTAGE"))
                            priceSale = price - price * (Double) row[4] / 100;

                        else if (row[5].equals("AMOUNT"))
                            priceSale = price - (Double) row[4];
                    }

                    return ProductOverviewDTO.builder()
                            .id((Long) row[0])
                            .name((String) row[1])
                            .price(price)
                            .thumbnailUrl((String) row[3])
                            .priceSale(priceSale)
                            .rating((Double) row[6])
                            .build();
                })
                .toList();
    }
}
