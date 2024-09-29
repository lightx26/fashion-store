package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;
import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.dtos.response.ProductPageResponse;
import com.pbl.fashionstore.entities.Product;
import com.pbl.fashionstore.repositories.ProductRepository;
import com.pbl.fashionstore.repositories.specifications.ProductSpecifications;
import com.pbl.fashionstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductPageResponse getProductsByFilter(ProductFilterCriteriaParams filterCriteria) {
        Specification<Product> specification = getSpecification(filterCriteria);

        int limit = filterCriteria.getLimit();
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(0, limit, sort);

        Page<Product> products = productRepository.findAll(specification, pageable);

        List<Long> productIds = products.map(Product::getId).toList();
        List<ProductOverviewDTO> productDTOs = productRepository.getProductOverviewIn(productIds);
        productDTOs.sort(Comparator.comparing(ProductOverviewDTO::getId));

        return ProductPageResponse.builder()
                .isLast(products.isLast())
                .totalProducts(products.getTotalElements())
                .content(productDTOs)
                .build();
    }

    private Specification<Product> getSpecification(ProductFilterCriteriaParams filterCriteria) {
        Specification<Product> specification = Specification.where(null);

        if (filterCriteria.getCategoryId() != null) {
            specification = specification.and(ProductSpecifications.hasCategory(filterCriteria.getCategoryId()));
        }

        if (filterCriteria.getSizeIds() != null && !filterCriteria.getSizeIds().isEmpty()
                && filterCriteria.getColorIds() != null && !filterCriteria.getColorIds().isEmpty()) {
            specification = specification.and(ProductSpecifications.hasColorsAndSizes(filterCriteria.getColorIds(), filterCriteria.getSizeIds()));
        }

        else if (filterCriteria.getColorIds() != null && !filterCriteria.getColorIds().isEmpty()) {
            specification = specification.and(ProductSpecifications.hasColors(filterCriteria.getColorIds()));
        }

        else if (filterCriteria.getSizeIds() != null && !filterCriteria.getSizeIds().isEmpty()) {
            specification = specification.and(ProductSpecifications.hasSizes(filterCriteria.getSizeIds()));
        }

        if (filterCriteria.getMinPrice() != null || filterCriteria.getMaxPrice() != null) {
            specification = specification.and(ProductSpecifications.hasPriceInRange(filterCriteria.getMinPrice(), filterCriteria.getMaxPrice()));
        }

        if (filterCriteria.getStyleIds() != null && !filterCriteria.getStyleIds().isEmpty()) {
            specification = specification.and(ProductSpecifications.hasStyles(filterCriteria.getStyleIds()));
        }

        if (filterCriteria.getCursorId() != null) {
            specification = specification.and(ProductSpecifications.hasIdSmallerThan(filterCriteria.getCursorId()));
        }

        return specification;
    }
}
