package com.pbl.fashionstore.dtos.response;

import com.pbl.fashionstore.dtos.dto.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDetailsResponse {
    private ProductDetailsDTO productDetails;
    private VariantTypesDTO variants;
}
