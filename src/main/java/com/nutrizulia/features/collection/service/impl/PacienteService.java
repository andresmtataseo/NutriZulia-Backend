package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.PacienteDto;
import com.nutrizulia.features.collection.mapper.PacienteMapper;
import com.nutrizulia.features.collection.model.Paciente;
import com.nutrizulia.features.collection.repository.PacienteRepository;
import com.nutrizulia.features.collection.service.IPacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PacienteService implements IPacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    public List<PacienteDto> sycnPacientes(List<PacienteDto> pacientes) {
        List<PacienteDto> pacientesActualizadosDesdeServidor = new ArrayList<>();

        for (PacienteDto dto : pacientes) {
            Optional<Paciente> existenteOpt = pacienteRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                Paciente nuevo = pacienteMapper.toEntity(dto);
                pacienteRepository.save(nuevo);
            } else {
                Paciente existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    Paciente actualizado = pacienteMapper.toEntity(dto);
                    pacienteRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    pacientesActualizadosDesdeServidor.add(pacienteMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return pacientesActualizadosDesdeServidor;
    }

}
