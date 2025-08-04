package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.DetalleVitalDto;

import java.util.List;

public interface IDetalleVitalService {
    List<DetalleVitalDto> syncDetallesVital(List<DetalleVitalDto> detallesVitales);
}
