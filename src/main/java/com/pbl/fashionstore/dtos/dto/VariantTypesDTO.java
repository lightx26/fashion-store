package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class VariantTypesDTO {
    private ProductVariantDTO defaultVariant;
    private Set<ColorDTO> colors;
    private Set<SizeDTO> sizes;
}
