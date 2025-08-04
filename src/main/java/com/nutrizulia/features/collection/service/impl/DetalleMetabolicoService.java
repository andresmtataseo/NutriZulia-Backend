package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;
import com.nutrizulia.features.collection.mapper.DetalleMetabolicoMapper;
import com.nutrizulia.features.collection.repository.DetalleMetabolicoRepository;
import com.nutrizulia.features.collection.service.IDetalleMetabolicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleMetabolicoService implements IDetalleMetabolicoService {

    private final DetalleMetabolicoRepository detalleMetabolicoRepository;
    private final DetalleMetabolicoMapper detalleMetabolicoMapper;

    @Override
    public List<DetalleMetabolicoDto> syncDetallesMetabolico(List<DetalleMetabolicoDto> detallesMetabolicos) {
        return List.of();
    }
}
