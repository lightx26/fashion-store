package com.pbl.fashionstore.repositories.extensions;

import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;

import java.util.List;

public interface ProductRepositoryExtension {
    List<ProductOverviewDTO> getProductOverviewIn(List<Long> productIds);
}
