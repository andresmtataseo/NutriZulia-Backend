package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleVitalDto;

import java.util.List;

public interface IDetalleVitalService {
    BatchSyncResponseDTO syncDetallesVital(List<DetalleVitalDto> detallesVitales);
}
