package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleObstetriciaDto;

import java.util.List;

public interface IDetalleObstetriciaService {
    BatchSyncResponseDTO syncDetallesObstetricia(List<DetalleObstetriciaDto> detallesObstetricias);
}
