package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.EvaluacionAntropometricaDto;
import com.nutrizulia.features.collection.mapper.EvaluacionAntropometricaMapper;
import com.nutrizulia.features.collection.repository.EvaluacionAntropometricaRepository;
import com.nutrizulia.features.collection.service.IEvaluacionAntropometricaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EvaluacionAntropometricaService implements IEvaluacionAntropometricaService {

    private final EvaluacionAntropometricaRepository evaluacionAntropometricaRepository;
    private final EvaluacionAntropometricaMapper evaluacionAntropometricaMapper;

    @Override
    public List<EvaluacionAntropometricaDto> syncEvaluacionesAntropometrica(List<EvaluacionAntropometricaDto> evaluacionesAntropometrica) {
        return List.of();
    }
}
