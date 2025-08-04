package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.mapper.ActividadMapper;
import com.nutrizulia.features.collection.repository.ActividadRepository;
import com.nutrizulia.features.collection.service.IActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActividadService implements IActividadService {

    private final ActividadRepository actividadRepository;
    private final ActividadMapper actividadMapper;

    @Override
    public List<ActividadDto> getActividades() {
        return actividadRepository.findAll().stream().map(actividadMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ActividadDto> sycnActividades(List<ActividadDto> actividades) {
        return List.of();
    }
}
