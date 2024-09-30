package com.pbl.fashionstore.mappers;

import com.pbl.fashionstore.dtos.dto.CategoryDTO;
import com.pbl.fashionstore.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    public abstract CategoryDTO toDTO(Category category);
}
