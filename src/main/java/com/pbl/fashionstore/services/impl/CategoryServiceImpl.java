package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.CategoryDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;
import com.pbl.fashionstore.mappers.CategoryMapper;
import com.pbl.fashionstore.repositories.CategoryRepository;
import com.pbl.fashionstore.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CustomListResponse<CategoryDTO> getAllCategories() {

        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();

        return CustomListResponse.<CategoryDTO>builder()
                .content(categories)
                .build();
    }

}
