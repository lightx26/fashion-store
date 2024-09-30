package com.pbl.fashionstore.repositories.extensions;

import com.pbl.fashionstore.dtos.dto.ProductVariantDTO;
import com.pbl.fashionstore.dtos.dto.VariantTypesDTO;

import java.util.Optional;


public interface ProductVariantRepositoryExtension {
    VariantTypesDTO findProductVariantsByProductId(Long productId);
    Optional<ProductVariantDTO> findProductVariantByFilter(Long productId, Long colorId, Long sizeId);
}
