package com.pbl.fashionstore.mappers;

import com.pbl.fashionstore.dtos.dto.StyleDTO;
import com.pbl.fashionstore.entities.Style;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StyleMapper {
    public abstract StyleDTO toDTO(Style style);
}
