package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.EvaluacionAntropometricaDto;

import java.util.List;

public interface IEvaluacionAntropometricaService {
    BatchSyncResponseDTO syncEvaluacionesAntropometrica(List<EvaluacionAntropometricaDto> evaluacionesAntropometrica);
}
