package com.pbl.fashionstore.repositories.specifications;

import com.pbl.fashionstore.entities.Product;
import com.pbl.fashionstore.entities.ProductVariant;
import com.pbl.fashionstore.entities.StyleProduct;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("categoryId"), categoryId);
    }

    public static Specification<Product> hasColors(List<Long> colorIds) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ProductVariant> productVariant = subquery.from(ProductVariant.class);
            subquery.select(productVariant.get("productId"))
                    .where(productVariant.get("colorId").in(colorIds))
                    .distinct(true);

            return root.get("id").in(subquery);
        };
    }

    public static Specification<Product> hasSizes(List<Long> sizeIds) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ProductVariant> productVariant = subquery.from(ProductVariant.class);
            subquery.select(productVariant.get("productId"))
                    .where(productVariant.get("sizeId").in(sizeIds))
                    .distinct(true);

            return root.get("id").in(subquery);
        };
    }

    public static Specification<Product> hasColorsAndSizes(List<Long> colorIds, List<Long> sizeIds) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ProductVariant> productVariant = subquery.from(ProductVariant.class);
            subquery.select(productVariant.get("productId"))
                    .where(
                            productVariant.get("colorId").in(colorIds),
                            productVariant.get("sizeId").in(sizeIds)
                    )
                    .distinct(true);

            return root.get("id").in(subquery);
        };
    }

    public static Specification<Product> hasPriceInRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }

            if (minPrice == null) {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }

            if (maxPrice == null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }

            return cb.between(root.get("price"), minPrice, maxPrice);
        };
    }

    public static Specification<Product> hasStyles(List<Long> styleIds) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<StyleProduct> styleProduct = subquery.from(StyleProduct.class);

            subquery.select(styleProduct.get("productId"))
                    .where(styleProduct.get("styleId").in(styleIds))
                    .distinct(true);

            return root.get("id").in(subquery);
        };
    }

    public static Specification<Product> hasIdSmallerThan(Long id) {
        return (root, query, cb) -> cb.lessThan(root.get("id"), id);
    }
}
