package com.pbl.fashionstore.repositories.extensions;

import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;

import java.util.List;

public interface ProductRepositoryExtension {
    List<ProductOverviewDTO> findProductsByFilters(ProductFilterCriteriaParams filterParams);
    Long countProductsByFilters(ProductFilterCriteriaParams filterParams);
    boolean isLastByFilters(ProductFilterCriteriaParams filterParams);
}
