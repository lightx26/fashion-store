package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.dtos.dto.SizeDTO;
import com.pbl.fashionstore.dtos.response.CustomListResponse;
import com.pbl.fashionstore.mappers.SizeMapper;
import com.pbl.fashionstore.repositories.SizeRepository;
import com.pbl.fashionstore.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    @Override
    public CustomListResponse<SizeDTO> getAllSizes() {
        List<SizeDTO> sizes = sizeRepository.findAll().stream()
                .map(sizeMapper::toDTO)
                .toList();

        return CustomListResponse.<SizeDTO>builder()
                .content(sizes)
                .build();
    }
}
