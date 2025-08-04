package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetallePedriatricoDto;
import com.nutrizulia.features.collection.mapper.DetallePedriatricoMapper;
import com.nutrizulia.features.collection.model.DetallePedriatrico;
import com.nutrizulia.features.collection.repository.DetallePediatricoRepository;
import com.nutrizulia.features.collection.service.IDetallePediatricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DetallePediatricoService implements IDetallePediatricoService {

    private final DetallePediatricoRepository detallePediatricoRepository;
    private final DetallePedriatricoMapper detallePedriatricoMapper;

    @Override
    public List<DetallePedriatricoDto> syncDetallesPediatrico(List<DetallePedriatricoDto> detallesPedriatricos) {
        List<DetallePedriatricoDto> detallesActualizadosDesdeServidor = new ArrayList<>();

        for (DetallePedriatricoDto dto : detallesPedriatricos) {
            Optional<DetallePedriatrico> existenteOpt = detallePediatricoRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                DetallePedriatrico nuevo = detallePedriatricoMapper.toEntity(dto);
                detallePediatricoRepository.save(nuevo);
            } else {
                DetallePedriatrico existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    DetallePedriatrico actualizado = detallePedriatricoMapper.toEntity(dto);
                    detallePediatricoRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    detallesActualizadosDesdeServidor.add(detallePedriatricoMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return detallesActualizadosDesdeServidor;
    }
}
