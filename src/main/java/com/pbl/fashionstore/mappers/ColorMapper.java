package com.pbl.fashionstore.mappers;

import com.pbl.fashionstore.dtos.dto.ColorDTO;
import com.pbl.fashionstore.entities.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ColorMapper {
    public abstract ColorDTO toDTO(Color color);
}
