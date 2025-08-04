package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;
import com.nutrizulia.features.collection.mapper.PacienteRepresentanteMapper;
import com.nutrizulia.features.collection.repository.PacienteRepresentanteRepository;
import com.nutrizulia.features.collection.service.IPacienteRepresentanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PacienteRepresentanteService implements IPacienteRepresentanteService {

    private final PacienteRepresentanteRepository pacienteRepresentanteRepository;
    private final PacienteRepresentanteMapper pacienteRepresentanteMapper;

    @Override
    public List<PacienteRepresentanteDto> syncPacientesRepresentantes(List<PacienteRepresentanteDto> pacienteRepresentantes) {
        return List.of();
    }
}
