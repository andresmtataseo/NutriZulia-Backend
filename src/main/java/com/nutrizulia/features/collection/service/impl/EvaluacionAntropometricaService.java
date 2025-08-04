package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.EvaluacionAntropometricaDto;
import com.nutrizulia.features.collection.mapper.EvaluacionAntropometricaMapper;
import com.nutrizulia.features.collection.model.EvaluacionAntropometrica;
import com.nutrizulia.features.collection.repository.EvaluacionAntropometricaRepository;
import com.nutrizulia.features.collection.service.IEvaluacionAntropometricaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EvaluacionAntropometricaService implements IEvaluacionAntropometricaService {

    private final EvaluacionAntropometricaRepository evaluacionAntropometricaRepository;
    private final EvaluacionAntropometricaMapper evaluacionAntropometricaMapper;

    @Override
    public List<EvaluacionAntropometricaDto> syncEvaluacionesAntropometrica(List<EvaluacionAntropometricaDto> evaluacionesAntropometrica) {
        List<EvaluacionAntropometricaDto> evaluacionesActualizadasDesdeServidor = new ArrayList<>();

        for (EvaluacionAntropometricaDto dto : evaluacionesAntropometrica) {
            Optional<EvaluacionAntropometrica> existenteOpt = evaluacionAntropometricaRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                EvaluacionAntropometrica nueva = evaluacionAntropometricaMapper.toEntity(dto);
                evaluacionAntropometricaRepository.save(nueva);
            } else {
                EvaluacionAntropometrica existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    EvaluacionAntropometrica actualizada = evaluacionAntropometricaMapper.toEntity(dto);
                    evaluacionAntropometricaRepository.save(actualizada);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    evaluacionesActualizadasDesdeServidor.add(evaluacionAntropometricaMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return evaluacionesActualizadasDesdeServidor;
    }
}
