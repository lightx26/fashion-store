package com.pbl.fashionstore.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCountResponse {
    private Long totalProducts;
}
