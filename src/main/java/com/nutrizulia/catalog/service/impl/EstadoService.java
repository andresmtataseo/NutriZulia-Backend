package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.EstadoDto;
import com.nutrizulia.catalog.mapper.EstadoMapper;
import com.nutrizulia.catalog.repository.EstadoRepository;
import com.nutrizulia.catalog.service.IEstadoService;
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
