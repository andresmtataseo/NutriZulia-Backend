package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;
import com.nutrizulia.features.collection.mapper.ConsultaMapper;
import com.nutrizulia.features.collection.repository.ConsultaRepository;
import com.nutrizulia.features.collection.service.IPacienteRepresentanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ConsultaService implements IPacienteRepresentanteService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;

    @Override
    public List<PacienteRepresentanteDto> syncPacientesRepresentantes(List<PacienteRepresentanteDto> pacienteRepresentantes) {
        return List.of();
    }
}
