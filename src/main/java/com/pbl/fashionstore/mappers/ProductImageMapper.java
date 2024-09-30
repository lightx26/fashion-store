package com.pbl.fashionstore.mappers;

import com.pbl.fashionstore.dtos.dto.ProductImageDTO;
import com.pbl.fashionstore.entities.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductImageMapper {
    public abstract ProductImageDTO toDTO(ProductImage productImage);
}
