package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DiagnosticoDto;
import com.nutrizulia.features.collection.mapper.DiagnosticoMapper;
import com.nutrizulia.features.collection.model.Diagnostico;
import com.nutrizulia.features.collection.repository.DiagnosticoRepository;
import com.nutrizulia.features.collection.service.IDiagnosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiagnosticoService implements IDiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final DiagnosticoMapper diagnosticoMapper;

    @Override
    public List<DiagnosticoDto> syncDiagnosticos(List<DiagnosticoDto> diagnosticos) {
        List<DiagnosticoDto> diagnosticosActualizadosDesdeServidor = new ArrayList<>();

        for (DiagnosticoDto dto : diagnosticos) {
            Optional<Diagnostico> existenteOpt = diagnosticoRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                Diagnostico nuevo = diagnosticoMapper.toEntity(dto);
                diagnosticoRepository.save(nuevo);
            } else {
                Diagnostico existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    Diagnostico actualizado = diagnosticoMapper.toEntity(dto);
                    diagnosticoRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    diagnosticosActualizadosDesdeServidor.add(diagnosticoMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return diagnosticosActualizadosDesdeServidor;
    }
}
