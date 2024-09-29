package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.SizeDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;
import com.pbl.fashionstore.repositories.SizeRepository;
import com.pbl.fashionstore.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    @Override
    public CustomListResponse<SizeDTO> getAllSizes() {
        List<SizeDTO> sizes = sizeRepository.findAll().stream()
                .map(size -> SizeDTO.builder()
                        .id(size.getId())
                        .label(size.getLabel())
                        .build())
                .toList();

        return CustomListResponse.<SizeDTO>builder()
                .content(sizes)
                .build();
    }
}
