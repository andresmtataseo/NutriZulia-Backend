package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetallePedriatricoDto;

import java.util.List;

public interface IDetallePediatricoService {
    BatchSyncResponseDTO syncDetallesPediatrico(List<DetallePedriatricoDto> detallesPedriatricos);
}
