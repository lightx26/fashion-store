package com.pbl.fashionstore.services;

import com.pbl.fashionstore.dtos.dto.SizeDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;

public interface SizeService {
    CustomListResponse<SizeDTO> getAllSizes();
}
