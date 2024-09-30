package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.StyleDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;
import com.pbl.fashionstore.mappers.StyleMapper;
import com.pbl.fashionstore.repositories.StyleRepository;
import com.pbl.fashionstore.services.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StyleServiceImpl implements StyleService {
    private final StyleRepository styleRepository;
    private final StyleMapper styleMapper;

    public CustomListResponse<StyleDTO> getAllStyles() {
        List<StyleDTO> styles = styleRepository.findAll().stream()
                .map(styleMapper::toDTO)
                .toList();

        return CustomListResponse.<StyleDTO>builder()
                .content(styles)
                .build();
    }
}
