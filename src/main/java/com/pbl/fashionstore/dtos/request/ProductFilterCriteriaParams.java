package com.pbl.fashionstore.dtos.request;

import com.pbl.fashionstore.enums.SortOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductFilterCriteriaParams {
    private Long categoryId;
    private List<Long> colorIds;
    private List<Long> sizeIds;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Long> styleIds;
    private SortOption sortOption;
    private Object cursorValue;
    private Long cursorId;
    private Integer limit;

    public ProductFilterCriteriaParams(ProductFilterCriteriaParams filterCriteria) {
        this.categoryId = filterCriteria.getCategoryId();
        this.colorIds = filterCriteria.getColorIds();
        this.sizeIds = filterCriteria.getSizeIds();
        this.minPrice = filterCriteria.getMinPrice();
        this.maxPrice = filterCriteria.getMaxPrice();
        this.styleIds = filterCriteria.getStyleIds();
        this.sortOption = filterCriteria.getSortOption();
        this.cursorValue = filterCriteria.getCursorValue();
        this.cursorId = filterCriteria.getCursorId();
        this.limit = filterCriteria.getLimit();
    }
}
