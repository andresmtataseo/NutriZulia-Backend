package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.TipoActividadDto;
import com.nutrizulia.mapper.pre.TipoActividadMapper;
import com.nutrizulia.repository.pre.TipoActividadRepository;
import com.nutrizulia.service.pre.ITipoActividadService;
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
