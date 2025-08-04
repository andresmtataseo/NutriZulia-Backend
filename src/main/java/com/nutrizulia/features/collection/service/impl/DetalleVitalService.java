package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleVitalDto;
import com.nutrizulia.features.collection.mapper.DetalleVitalMapper;
import com.nutrizulia.features.collection.repository.DetalleVitalRepository;
import com.nutrizulia.features.collection.service.IDetalleVitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleVitalService implements IDetalleVitalService {

    private final DetalleVitalRepository detalleVitalRepository;
    private final DetalleVitalMapper detalleVitalMapper;

    @Override
    public List<DetalleVitalDto> syncDetallesVital(List<DetalleVitalDto> detallesVitales) {
        return List.of();
    }
}
