package com.pbl.fashionstore.services;

import com.pbl.fashionstore.dtos.dto.ColorDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;

public interface ColorService {
    CustomListResponse<ColorDTO> getAllColors();
}
