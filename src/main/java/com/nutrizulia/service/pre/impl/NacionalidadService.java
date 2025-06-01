package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.NacionalidadDto;
import com.nutrizulia.mapper.pre.NacionalidadMapper;
import com.nutrizulia.repository.pre.NacionalidadRepository;
import com.nutrizulia.service.pre.INacionalidadService;
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
