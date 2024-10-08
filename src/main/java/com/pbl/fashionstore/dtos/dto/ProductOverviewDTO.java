package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
public class ProductOverviewDTO {
    private Long id;
    private String name;
    private String thumbnailUrl;
    private BigDecimal price;
    private BigDecimal priceSale;
    private BigDecimal rating;
    private Instant createdAt;
}
