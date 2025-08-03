package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.PacienteDto;

import java.util.List;

public interface IPacienteService {

    List<PacienteDto> sycnPacientes(List<PacienteDto> pacientes);

}
