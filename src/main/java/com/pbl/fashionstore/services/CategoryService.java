package com.pbl.fashionstore.services;

import com.pbl.fashionstore.dtos.dto.CategoryDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;

public interface CategoryService {
    CustomListResponse<CategoryDTO> getAllCategories();
}
