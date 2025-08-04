package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetallePedriatricoDto;
import com.nutrizulia.features.collection.mapper.DetallePedriatricoMapper;
import com.nutrizulia.features.collection.repository.DetallePediatricoRepository;
import com.nutrizulia.features.collection.service.IDetallePediatricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetallePediatricoService implements IDetallePediatricoService {

    private final DetallePediatricoRepository detallePediatricoRepository;
    private final DetallePedriatricoMapper detallePedriatricoMapper;

    @Override
    public List<DetallePedriatricoDto> syncDetallesPediatrico(List<DetallePedriatricoDto> detallesPedriatricos) {
        return List.of();
    }
}
