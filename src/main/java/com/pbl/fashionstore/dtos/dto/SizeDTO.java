package com.pbl.fashionstore.dtos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class SizeDTO {
    private Long id;
    private String label;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SizeDTO sizeDTO)) return false;
        return this.id.equals(sizeDTO.getId()) && this.label.equals(sizeDTO.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
}
