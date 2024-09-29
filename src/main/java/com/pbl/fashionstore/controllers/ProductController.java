package com.pbl.fashionstore.controllers;

import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam Long categoryId,
            @RequestParam(required = false) List<Long> colorIds,
            @RequestParam(required = false) List<Long> sizeIds,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<Long> styleIds,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "12") Integer limit
    ) {
        ProductFilterCriteriaParams criteria = new ProductFilterCriteriaParams(
                categoryId,
                colorIds,
                sizeIds,
                minPrice,
                maxPrice,
                styleIds,
                cursorId,
                limit
        );

        return ResponseEntity.ok(productService.getProductsByFilter(criteria));
    }
}
