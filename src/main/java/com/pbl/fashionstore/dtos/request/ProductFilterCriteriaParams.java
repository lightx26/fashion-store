package com.pbl.fashionstore.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductFilterCriteriaParams {
    private Long categoryId;
    private List<Long> colorIds;
    private List<Long> sizeIds;
    private Double minPrice;
    private Double maxPrice;
    private List<Long> styleIds;
    private Long cursorId;
    private Integer limit;
}
