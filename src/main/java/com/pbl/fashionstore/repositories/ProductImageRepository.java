package com.pbl.fashionstore.repositories;

import com.pbl.fashionstore.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findProductImagesByProductVariantId(Long productVariantId);
}
