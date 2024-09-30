package com.pbl.fashionstore.services;

import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.dtos.response.CountResponse;
import com.pbl.fashionstore.dtos.response.ProductPageResponse;


public interface ProductService {
    ProductPageResponse getProductsByFilter(ProductFilterCriteriaParams filterCriteria);
    CountResponse countProductsByFilter(ProductFilterCriteriaParams filterCriteria);
}
