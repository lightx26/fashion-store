package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.dtos.response.CountResponse;
import com.pbl.fashionstore.dtos.response.ProductPageResponse;
import com.pbl.fashionstore.enums.SortOption;
import com.pbl.fashionstore.repositories.ProductRepository;
import com.pbl.fashionstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductPageResponse getProductsByFilter(ProductFilterCriteriaParams filterParams) {

        List<ProductOverviewDTO> productDTOs = productRepository.findProductsByFilters(filterParams);

        boolean isLast;
        if (productDTOs.size() < filterParams.getLimit()) {
            isLast = true;
        } else {
            isLast = isLastByFilters(productDTOs.getLast(), filterParams);
        }

        return ProductPageResponse.builder()
                .isLast(isLast)
                .content(productDTOs)
                .build();
    }

    @Override
    public CountResponse countProductsByFilter(ProductFilterCriteriaParams filterCriteria) {
        return CountResponse.builder()
                .totalElements(productRepository.countProductsByFilters(filterCriteria))
                .build();
    }

    private boolean isLastByFilters(ProductOverviewDTO lastElement, ProductFilterCriteriaParams currentFilter) {
        ProductFilterCriteriaParams nextFilterParams = new ProductFilterCriteriaParams(currentFilter);

        if (currentFilter.getSortOption() != null) {
            if (currentFilter.getSortOption().equals(SortOption.NEWEST)) {
                nextFilterParams.setCursorValue(lastElement.getCreatedAt());
            } else if (currentFilter.getSortOption().equals(SortOption.RATING)) {
                nextFilterParams.setCursorValue(lastElement.getRating());
            }
        }

        nextFilterParams.setCursorId(lastElement.getId());

        return productRepository.isLastByFilters(nextFilterParams);
    }

}
