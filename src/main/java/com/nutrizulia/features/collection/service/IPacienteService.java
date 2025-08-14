package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.dto.PacienteDto;

import java.util.List;

public interface IPacienteService {

    BatchSyncResponseDTO syncPacientes(List<PacienteDto> pacientes);
    
    FullSyncResponseDTO<PacienteDto> findAllActive();

}
