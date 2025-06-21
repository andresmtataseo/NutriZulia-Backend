package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.EspecialidadDto;
import com.nutrizulia.catalog.mapper.EspecialidadMapper;
import com.nutrizulia.catalog.repository.EspecialidadRepository;
import com.nutrizulia.catalog.service.IEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EspecialidadService implements IEspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;

    @Override
    public List<EspecialidadDto> getEspecialidades() {
        return especialidadRepository.findAll().stream().map(especialidadMapper::toDto).collect(Collectors.toList());
    }
}
