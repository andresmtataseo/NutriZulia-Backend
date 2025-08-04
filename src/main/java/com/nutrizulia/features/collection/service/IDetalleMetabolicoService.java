package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;

import java.util.List;

public interface IDetalleMetabolicoService {
    List<DetalleMetabolicoDto> syncDetallesMetabolico(List<DetalleMetabolicoDto> detallesMetabolicos);
}
