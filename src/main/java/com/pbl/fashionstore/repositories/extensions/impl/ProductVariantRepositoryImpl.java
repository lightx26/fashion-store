package com.pbl.fashionstore.repositories.extensions.impl;

import com.pbl.fashionstore.dtos.dto.ColorDTO;
import com.pbl.fashionstore.dtos.dto.ProductVariantDTO;
import com.pbl.fashionstore.dtos.dto.SizeDTO;
import com.pbl.fashionstore.dtos.dto.VariantTypesDTO;
import com.pbl.fashionstore.repositories.extensions.ProductVariantRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.HashSet;
import java.util.List;
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
}
