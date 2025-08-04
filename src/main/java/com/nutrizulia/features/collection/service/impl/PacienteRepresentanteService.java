package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;
import com.nutrizulia.features.collection.mapper.PacienteRepresentanteMapper;
import com.nutrizulia.features.collection.model.PacienteRepresentante;
import com.nutrizulia.features.collection.repository.PacienteRepresentanteRepository;
import com.nutrizulia.features.collection.service.IPacienteRepresentanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PacienteRepresentanteService implements IPacienteRepresentanteService {

    private final PacienteRepresentanteRepository pacienteRepresentanteRepository;
    private final PacienteRepresentanteMapper pacienteRepresentanteMapper;

    @Override
    public List<PacienteRepresentanteDto> syncPacientesRepresentantes(List<PacienteRepresentanteDto> pacienteRepresentantes) {
        List<PacienteRepresentanteDto> pacienteRepresentantesActualizadosDesdeServidor = new ArrayList<>();

        for (PacienteRepresentanteDto dto : pacienteRepresentantes) {
            Optional<PacienteRepresentante> existenteOpt = pacienteRepresentanteRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                PacienteRepresentante nuevo = pacienteRepresentanteMapper.toEntity(dto);
                pacienteRepresentanteRepository.save(nuevo);
            } else {
                PacienteRepresentante existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    PacienteRepresentante actualizado = pacienteRepresentanteMapper.toEntity(dto);
                    pacienteRepresentanteRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    pacienteRepresentantesActualizadosDesdeServidor.add(pacienteRepresentanteMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return pacienteRepresentantesActualizadosDesdeServidor;
    }
}
