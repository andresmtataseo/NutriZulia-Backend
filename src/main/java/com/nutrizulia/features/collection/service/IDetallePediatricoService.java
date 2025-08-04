package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.DetallePedriatricoDto;

import java.util.List;

public interface IDetallePediatricoService {
    List<DetallePedriatricoDto> syncDetallesPediatrico(List<DetallePedriatricoDto> detallesPedriatricos);
}
