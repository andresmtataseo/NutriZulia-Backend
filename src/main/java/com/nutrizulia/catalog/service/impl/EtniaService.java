package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.EtniaDto;
import com.nutrizulia.catalog.mapper.EtniaMapper;
import com.nutrizulia.catalog.repository.EtniaRepository;
import com.nutrizulia.catalog.service.IEtniaService;
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
