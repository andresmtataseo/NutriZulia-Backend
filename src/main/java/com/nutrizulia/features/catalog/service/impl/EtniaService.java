package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.EtniaDto;
import com.nutrizulia.features.catalog.mapper.EtniaMapper;
import com.nutrizulia.features.catalog.repository.EtniaRepository;
import com.nutrizulia.features.catalog.service.IEtniaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtniaService implements IEtniaService {

    private final EtniaRepository etniaRepository;
    private final EtniaMapper etniaMapper;

    @Override
    public List<EtniaDto> getEtnias() {
        return etniaRepository.findAll()
                .stream()
                .map(etniaMapper::toDto)
                .collect(Collectors.toList());
    }
}
