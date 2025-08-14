package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.dto.RepresentanteDto;

import java.util.List;

public interface IRepresentanteService {
    BatchSyncResponseDTO syncRepresentantes(List<RepresentanteDto> representantes);
    FullSyncResponseDTO<RepresentanteDto> findAllActive();
}
