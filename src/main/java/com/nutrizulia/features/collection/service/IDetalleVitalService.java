package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleVitalDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;

import java.util.List;

public interface IDetalleVitalService {
    BatchSyncResponseDTO syncDetallesVital(List<DetalleVitalDto> detallesVitales);
    FullSyncResponseDTO<DetalleVitalDto> findAllActive();
}
