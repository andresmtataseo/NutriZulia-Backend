package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.EstadoDto;
import com.nutrizulia.features.catalog.mapper.EstadoMapper;
import com.nutrizulia.features.catalog.repository.EstadoRepository;
import com.nutrizulia.features.catalog.service.IEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadoService implements IEstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    @Override
    public List<EstadoDto> getEstados() {
            return estadoRepository.findAll()
                    .stream()
                    .map(estadoMapper::toDto)
                    .collect(Collectors.toList());
    }

}
