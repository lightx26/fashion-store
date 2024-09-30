package com.pbl.fashionstore.repositories.extensions;

import com.pbl.fashionstore.dtos.dto.VariantTypesDTO;


public interface ProductVariantRepositoryExtension {
    VariantTypesDTO findProductVariantsByProductId(Long productId);
}
