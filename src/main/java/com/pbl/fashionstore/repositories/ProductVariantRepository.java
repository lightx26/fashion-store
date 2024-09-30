package com.pbl.fashionstore.repositories;

import com.pbl.fashionstore.entities.ProductVariant;
import com.pbl.fashionstore.repositories.extensions.ProductVariantRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>, ProductVariantRepositoryExtension {
}

