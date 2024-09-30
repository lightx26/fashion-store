package com.pbl.fashionstore.mappers;

import com.pbl.fashionstore.dtos.dto.SizeDTO;
import com.pbl.fashionstore.entities.Size;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class SizeMapper {
    public abstract SizeDTO toDTO(Size size);
}
