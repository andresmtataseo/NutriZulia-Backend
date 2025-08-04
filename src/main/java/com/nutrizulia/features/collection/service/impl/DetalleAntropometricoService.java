package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleAntropometricoDto;
import com.nutrizulia.features.collection.mapper.DetalleAntropometricoMapper;
import com.nutrizulia.features.collection.repository.DetalleAntropometricoRepository;
import com.nutrizulia.features.collection.service.IDetalleAntropometricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleAntropometricoService implements IDetalleAntropometricoService {

    private final DetalleAntropometricoRepository detalleAntropometricoRepository;
    private final DetalleAntropometricoMapper detalleAntropometricoMapper;

    @Override
    public List<DetalleAntropometricoDto> syncDetallesAntropometrico(List<DetalleAntropometricoDto> detallesAntropometricos) {
        return List.of();
    }
}
