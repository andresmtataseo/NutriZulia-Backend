package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;

import java.util.List;

public interface IPacienteRepresentanteService {
    List<PacienteRepresentanteDto> syncPacientesRepresentantes(List<PacienteRepresentanteDto> pacienteRepresentantes);
}
