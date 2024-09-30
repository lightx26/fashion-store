package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductImageDTO {
    private String imageUrl;
    private Integer position;
}
