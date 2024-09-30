package com.pbl.fashionstore.services;

import com.pbl.fashionstore.dtos.dto.ProductVariantDTO;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.dtos.response.CountResponse;
import com.pbl.fashionstore.dtos.response.ProductDetailsResponse;
import com.pbl.fashionstore.dtos.response.ProductPageResponse;


public interface ProductService {
    ProductPageResponse getProductsByFilter(ProductFilterCriteriaParams filterCriteria);
    CountResponse countProductsByFilter(ProductFilterCriteriaParams filterCriteria);
    ProductDetailsResponse getProductById(Long productId);
    ProductVariantDTO getProductVariant(Long productId, Long colorId, Long sizeId);
}
