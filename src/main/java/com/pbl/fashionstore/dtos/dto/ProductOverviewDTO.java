package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductOverviewDTO {
    private Long id;
    private String name;
    private String thumbnailUrl;
    private Double price;
    private Double priceSale;
    private Double rating;
}
