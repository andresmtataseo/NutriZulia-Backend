package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;

import java.util.List;

public interface IActividadService {

    List<ActividadDto> getActividades();

    BatchSyncResponseDTO sycnActividades(List<ActividadDto> actividades);

}
