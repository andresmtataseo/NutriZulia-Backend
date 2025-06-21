package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.TipoActividadDto;
import com.nutrizulia.catalog.mapper.TipoActividadMapper;
import com.nutrizulia.catalog.repository.TipoActividadRepository;
import com.nutrizulia.catalog.service.ITipoActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoActividadService implements ITipoActividadService {

    private final TipoActividadRepository tipoActividadRepository;
    private final TipoActividadMapper tipoActividadMapper;

    @Override
    public List<TipoActividadDto> getTiposActividades() {
        return tipoActividadRepository.findAll().stream().map(tipoActividadMapper::toDto).collect(Collectors.toList());
    }
}
