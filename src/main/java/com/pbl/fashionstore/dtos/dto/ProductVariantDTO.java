package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductVariantDTO {
    private Long id;
    private ColorDTO color;
    private SizeDTO size;
    private List<ProductImageDTO> images;
    private Integer stock;
}
