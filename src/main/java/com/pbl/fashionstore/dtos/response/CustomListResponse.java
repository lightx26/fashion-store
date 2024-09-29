package com.pbl.fashionstore.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CustomListResponse<T> {
    private List<T> content;
}
