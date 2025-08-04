package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.EvaluacionAntropometricaDto;

import java.util.List;

public interface IEvaluacionAntropometricaService {
    List<EvaluacionAntropometricaDto> syncEvaluacionesAntropometrica(List<EvaluacionAntropometricaDto> evaluacionesAntropometrica);
}
