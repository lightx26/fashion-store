package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
public class ProductDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal rating;
    private Long reviewCount;
    private BigDecimal price;
    private BigDecimal priceSale;
    private Integer watchersCount;
    private Instant discountExpiration;
}
