package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.DetalleAntropometricoDto;

import java.util.List;

public interface IDetalleAntropometricoService {
    List<DetalleAntropometricoDto> syncDetallesAntropometrico(List<DetalleAntropometricoDto> detallesAntropometricos);
}
