package com.pbl.fashionstore.dtos.response;

import com.pbl.fashionstore.dtos.dto.ProductOverviewDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductPageResponse {
    private boolean isLast;
    private List<ProductOverviewDTO> content;
}
