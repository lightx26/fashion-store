package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class ColorDTO {
    private Long id;
    private String hexCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColorDTO colorDTO)) return false;
        return Objects.equals(this.id, colorDTO.getId()) && this.hexCode.equals(colorDTO.getHexCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hexCode);
    }
}
