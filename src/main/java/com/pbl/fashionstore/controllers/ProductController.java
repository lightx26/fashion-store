package com.pbl.fashionstore.controllers;

import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.dtos.response.CustomListResponse;
import com.pbl.fashionstore.enums.SortOption;
import com.pbl.fashionstore.services.ProductService;
import com.pbl.fashionstore.services.ProductWatchingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductWatchingService productWatchingService;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> colorIds,
            @RequestParam(required = false) List<Long> sizeIds,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Long> styleIds,
            @RequestParam(required = false) SortOption sortOption,
            @RequestParam(required = false) String cursorValue,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "12") Integer limit
    ) {
        ProductFilterCriteriaParams criteria = ProductFilterCriteriaParams.builder()
                .categoryId(categoryId)
                .colorIds(colorIds)
                .sizeIds(sizeIds)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .styleIds(styleIds)
                .sortOption(sortOption)
                .cursorValue(cursorValue)
                .cursorId(cursorId)
                .limit(limit)
                .build();

        return ResponseEntity.ok(productService.getProductsByFilter(criteria));
    }

    @GetMapping("/sort-options")
    public ResponseEntity<?> getSortOptions() {
        return ResponseEntity.ok(
                CustomListResponse.builder()
                        .content(List.of(SortOption.values()))
                        .build()
        );
    }

    @GetMapping("/count")
    public ResponseEntity<?> getProductsCount(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> colorIds,
            @RequestParam(required = false) List<Long> sizeIds,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Long> styleIds
    ) {
        ProductFilterCriteriaParams criteria = ProductFilterCriteriaParams.builder()
                .categoryId(categoryId)
                .colorIds(colorIds)
                .sizeIds(sizeIds)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .styleIds(styleIds)
                .build();

        return ResponseEntity.ok(productService.countProductsByFilter(criteria));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId, HttpServletRequest request) {

        HttpSession session = request.getSession();
        productWatchingService.addWatcher(productId, session.getId());

        return ResponseEntity.ok(productService.getProductById(productId));
    }
}
