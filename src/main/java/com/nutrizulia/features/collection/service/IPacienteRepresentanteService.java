package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;

import java.util.List;

public interface IPacienteRepresentanteService {
    BatchSyncResponseDTO syncPacientesRepresentantes(List<PacienteRepresentanteDto> pacienteRepresentantes);
    FullSyncResponseDTO<PacienteRepresentanteDto> findAllActive();
}
