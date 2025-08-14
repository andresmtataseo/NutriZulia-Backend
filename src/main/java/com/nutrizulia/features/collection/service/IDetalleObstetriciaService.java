package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleObstetriciaDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;

import java.util.List;

public interface IDetalleObstetriciaService {
    BatchSyncResponseDTO syncDetallesObstetricia(List<DetalleObstetriciaDto> detallesObstetricias);
    FullSyncResponseDTO<DetalleObstetriciaDto> findAllActive();
}
