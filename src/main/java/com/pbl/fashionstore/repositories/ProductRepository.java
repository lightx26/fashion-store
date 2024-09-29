package com.pbl.fashionstore.repositories;

import com.pbl.fashionstore.entities.Product;
import com.pbl.fashionstore.repositories.extensions.ProductRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>, ProductRepositoryExtension {
}
