package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleObstetriciaDto;
import com.nutrizulia.features.collection.mapper.DetalleObstetriciaMapper;
import com.nutrizulia.features.collection.repository.DetalleObstetriciaRepository;
import com.nutrizulia.features.collection.service.IDetalleObstetriciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleObstetriciaService implements IDetalleObstetriciaService {

    private final DetalleObstetriciaRepository detalleObstetriciaRepository;
    private final DetalleObstetriciaMapper detalleObstetriciaMapper;

    @Override
    public List<DetalleObstetriciaDto> syncDetallesObstetricia(List<DetalleObstetriciaDto> detallesObstetricias) {
        return List.of();
    }
}
