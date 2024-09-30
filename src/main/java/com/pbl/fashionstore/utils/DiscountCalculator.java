package com.pbl.fashionstore.utils;

import com.pbl.fashionstore.enums.DiscountType;

import java.math.BigDecimal;

public class DiscountCalculator {
    public static BigDecimal calculatePriceSale(BigDecimal price, BigDecimal discountValue, DiscountType discountType) {
        if (discountType.equals(DiscountType.PERCENTAGE)) {
            return BigDecimal.valueOf(price.doubleValue() - price.doubleValue() * discountValue.doubleValue() / 100);
        } else if (discountType.equals(DiscountType.AMOUNT)) {
            return BigDecimal.valueOf(price.doubleValue() - discountValue.doubleValue());
        }
        return price;
    }
}
