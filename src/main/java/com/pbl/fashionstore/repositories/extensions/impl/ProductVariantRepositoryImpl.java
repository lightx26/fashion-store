package com.pbl.fashionstore.repositories.extensions.impl;

import com.pbl.fashionstore.dtos.dto.ColorDTO;
import com.pbl.fashionstore.dtos.dto.ProductVariantDTO;
import com.pbl.fashionstore.dtos.dto.SizeDTO;
import com.pbl.fashionstore.dtos.dto.VariantTypesDTO;
import com.pbl.fashionstore.repositories.extensions.ProductVariantRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ProductVariantRepositoryImpl implements ProductVariantRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public VariantTypesDTO findProductVariantsByProductId(Long productId) {
        String sql = "SELECT pv.product_variant_id, c.color_id, c.hex_code, s.size_id, s.label, pv.stock_count " +
                "FROM product_variant pv " +
                "INNER JOIN color c " +
                "ON pv.color_id = c.color_id " +
                "INNER JOIN size s " +
                "ON pv.size_id = s.size_id " +
                "WHERE pv.product_id = :productId ";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("productId", productId);

        List<Object[]> results = query.getResultList();

        Object[] firstResult = results.getFirst();
        ProductVariantDTO defaultVariant = ProductVariantDTO.builder()
                .id((Long) firstResult[0])
                .color(ColorDTO.builder()
                        .id((Long) firstResult[1])
                        .hexCode((String) firstResult[2])
                        .build()
                )
                .size(SizeDTO.builder()
                        .id((Long) firstResult[3])
                        .label((String) firstResult[4])
                        .build()
                )
                .stock(firstResult[5] != null ? (Integer) firstResult[5] : 0)
                .build();

        Set<ColorDTO> colors = new HashSet<>();
        Set<SizeDTO> sizes = new HashSet<>();

        for (Object[] row : results) {
            ColorDTO colorDTO = ColorDTO.builder()
                    .id((Long) row[1])
                    .hexCode((String) row[2])
                    .build();
            colors.add(colorDTO);

            SizeDTO sizeDTO = SizeDTO.builder()
                    .id((Long) row[3])
                    .label((String) row[4])
                    .build();
            sizes.add(sizeDTO);
        }

        return VariantTypesDTO.builder()
                .defaultVariant(defaultVariant)
                .colors(colors)
                .sizes(sizes)
                .build();
    }

    @Override
    public Optional<ProductVariantDTO> findProductVariantByFilter(Long productId, Long colorId, Long sizeId) {
        String sql = "SELECT pv.product_variant_id, c.color_id, c.hex_code, s.size_id, s.label, pv.stock_count " +
                "FROM product_variant pv " +
                "INNER JOIN color c " +
                "ON pv.color_id = c.color_id " +
                "INNER JOIN size s " +
                "ON pv.size_id = s.size_id " +
                "WHERE pv.product_id = :productId " +
                "AND pv.color_id = :colorId " +
                "AND pv.size_id = :sizeId " +
                "FETCH FIRST 1 ROW ONLY";

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("productId", productId);
        query.setParameter("colorId", colorId);
        query.setParameter("sizeId", sizeId);

        Object[] result;
        try {
            result = (Object[]) query.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        return Optional.of(ProductVariantDTO.builder()
                .id((Long) result[0])
                .color(ColorDTO.builder()
                        .id((Long) result[1])
                        .hexCode((String) result[2])
                        .build()
                )
                .size(SizeDTO.builder()
                        .id((Long) result[3])
                        .label((String) result[4])
                        .build()
                )
                .stock(result[5] != null ? (Integer) result[5] : 0)
                .build()
        );
    }
}
