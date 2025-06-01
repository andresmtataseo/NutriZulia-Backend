package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.EspecialidadDto;
import com.nutrizulia.mapper.pre.EspecialidadMapper;
import com.nutrizulia.repository.pre.EspecialidadRepository;
import com.nutrizulia.service.pre.IEspecialidadService;
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
