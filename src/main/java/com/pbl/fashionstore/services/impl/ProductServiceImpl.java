package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.*;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.dtos.response.CountResponse;
import com.pbl.fashionstore.dtos.response.ProductDetailsResponse;
import com.pbl.fashionstore.dtos.response.ProductPageResponse;
import com.pbl.fashionstore.enums.SortOption;
import com.pbl.fashionstore.exceptions.EntityNotFoundException;
import com.pbl.fashionstore.repositories.ProductImageRepository;
import com.pbl.fashionstore.repositories.ProductRepository;
import com.pbl.fashionstore.repositories.ProductVariantRepository;
import com.pbl.fashionstore.services.ProductService;
import com.pbl.fashionstore.services.ProductWatchingService;
import com.pbl.fashionstore.utils.ProductFilterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductWatchingService productWatchingService;

    public ProductPageResponse getProductsByFilter(ProductFilterCriteriaParams filterParams) {

        ProductFilterValidator.validateFilter(filterParams);

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

    @Override
    public ProductDetailsResponse getProductById(Long productId) {
        ProductDetailsDTO productDetailsDTO = productRepository.findProductDetailsById(productId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Product not found")
                );

        productDetailsDTO.setWatchersCount(productWatchingService.countWatchers(productId));
        VariantTypesDTO variantTypesDTO = productVariantRepository.findProductVariantsByProductId(productId);
        List<ProductImageDTO> images = productImageRepository.findProductImagesByProductVariantId(variantTypesDTO.getDefaultVariant().getId()).stream().map(
                productImage -> ProductImageDTO.builder()
                        .imageUrl(productImage.getImageUrl())
                        .position(productImage.getPosition())
                        .build()
        ).toList();
        variantTypesDTO.getDefaultVariant().setImages(images);

        return ProductDetailsResponse.builder()
                .productDetails(productDetailsDTO)
                .variants(variantTypesDTO)
                .build();
    }

    @Override
    public ProductVariantDTO getProductVariant(Long productId, Long colorId, Long sizeId) {
        ProductVariantDTO variantDTO = productVariantRepository.findProductVariantByFilter(productId, colorId, sizeId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Product variant not found")
                );

        List<ProductImageDTO> images = productImageRepository.findProductImagesByProductVariantId(variantDTO.getId()).stream().map(
                productImage -> ProductImageDTO.builder()
                        .imageUrl(productImage.getImageUrl())
                        .position(productImage.getPosition())
                        .build()
        ).toList();
        variantDTO.setImages(images);

        return variantDTO;
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
