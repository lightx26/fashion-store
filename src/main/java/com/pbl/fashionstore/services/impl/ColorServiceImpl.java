package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.ColorDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;
import com.pbl.fashionstore.mappers.ColorMapper;
import com.pbl.fashionstore.repositories.ColorRepository;
import com.pbl.fashionstore.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    @Override
    public CustomListResponse<ColorDTO> getAllColors() {
        List<ColorDTO> colors = colorRepository.findAll().stream()
                .map(colorMapper::toDTO)
                .toList();

        return CustomListResponse.<ColorDTO>builder()
                .content(colors)
                .build();
    }
}
