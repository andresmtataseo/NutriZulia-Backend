package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleAntropometricoDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;

import java.util.List;

public interface IDetalleAntropometricoService {
    BatchSyncResponseDTO syncDetallesAntropometrico(List<DetalleAntropometricoDto> detallesAntropometricos);
    FullSyncResponseDTO<DetalleAntropometricoDto> findAllActive();
}
