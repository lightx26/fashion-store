package com.pbl.fashionstore.repositories.extensions;

import com.pbl.fashionstore.dtos.dto.ProductDetailsDTO;
import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryExtension {
    List<ProductOverviewDTO> findProductsByFilters(ProductFilterCriteriaParams filterParams);
    Long countProductsByFilters(ProductFilterCriteriaParams filterParams);
    boolean isLastByFilters(ProductFilterCriteriaParams filterParams);
    Optional<ProductDetailsDTO> findProductDetailsById(Long id);
}
