package com.nutrizulia.collection.service;

import com.nutrizulia.collection.dto.PacienteDto;

import java.util.List;

public interface IPacienteService {

    List<PacienteDto> sycnPacientes(List<PacienteDto> pacientes);

}
