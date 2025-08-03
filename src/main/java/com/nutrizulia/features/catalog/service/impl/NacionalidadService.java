package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.NacionalidadDto;
import com.nutrizulia.features.catalog.mapper.NacionalidadMapper;
import com.nutrizulia.features.catalog.repository.NacionalidadRepository;
import com.nutrizulia.features.catalog.service.INacionalidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NacionalidadService implements INacionalidadService {

    private final NacionalidadRepository nacionalidadRepository;
    private final NacionalidadMapper nacionalidadMapper;

    @Override
    public List<NacionalidadDto> getNacionalidades() {
        return nacionalidadRepository.findAll()
                .stream()
                .map(nacionalidadMapper::toDto)
                .collect(Collectors.toList());
    }
}
