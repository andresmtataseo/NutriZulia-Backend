package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DiagnosticoDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;

import java.util.List;

public interface IDiagnosticoService {
    BatchSyncResponseDTO syncDiagnosticos(List<DiagnosticoDto> diagnosticos);
    FullSyncResponseDTO<DiagnosticoDto> findAllActive();
}
