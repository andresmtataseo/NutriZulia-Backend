package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.DetalleObstetriciaDto;

import java.util.List;

public interface IDetalleObstetriciaService {
    List<DetalleObstetriciaDto> syncDetallesObstetricia(List<DetalleObstetriciaDto> detallesObstetricias);
}
