package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;

import java.util.List;

public interface IDetalleMetabolicoService {
    BatchSyncResponseDTO syncDetallesMetabolico(List<DetalleMetabolicoDto> detallesMetabolicos);
}
