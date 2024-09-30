package com.pbl.fashionstore.dtos.dto;

import com.pbl.fashionstore.enums.DiscountType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductExternalsDTO {
    private Long id;
    private BigDecimal discountValue;
    private DiscountType discountType;
    private BigDecimal rating;
}
