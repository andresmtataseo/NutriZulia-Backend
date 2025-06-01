package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.EstadoDto;
import com.nutrizulia.mapper.pre.EstadoMapper;
import com.nutrizulia.repository.pre.EstadoRepository;
import com.nutrizulia.service.pre.IEstadoService;
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
