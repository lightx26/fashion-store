package com.pbl.fashionstore.services;

import com.pbl.fashionstore.dtos.dto.StyleDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;

public interface StyleService {
    CustomListResponse<StyleDTO> getAllStyles();
}
