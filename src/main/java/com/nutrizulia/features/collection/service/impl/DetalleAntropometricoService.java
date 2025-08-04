package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleAntropometricoDto;
import com.nutrizulia.features.collection.mapper.DetalleAntropometricoMapper;
import com.nutrizulia.features.collection.model.DetalleAntropometrico;
import com.nutrizulia.features.collection.repository.DetalleAntropometricoRepository;
import com.nutrizulia.features.collection.service.IDetalleAntropometricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DetalleAntropometricoService implements IDetalleAntropometricoService {

    private final DetalleAntropometricoRepository detalleAntropometricoRepository;
    private final DetalleAntropometricoMapper detalleAntropometricoMapper;

    @Override
    public List<DetalleAntropometricoDto> syncDetallesAntropometrico(List<DetalleAntropometricoDto> detallesAntropometricos) {
        List<DetalleAntropometricoDto> detallesActualizadosDesdeServidor = new ArrayList<>();

        for (DetalleAntropometricoDto dto : detallesAntropometricos) {
            Optional<DetalleAntropometrico> existenteOpt = detalleAntropometricoRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                DetalleAntropometrico nuevo = detalleAntropometricoMapper.toEntity(dto);
                detalleAntropometricoRepository.save(nuevo);
            } else {
                DetalleAntropometrico existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    DetalleAntropometrico actualizado = detalleAntropometricoMapper.toEntity(dto);
                    detalleAntropometricoRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    detallesActualizadosDesdeServidor.add(detalleAntropometricoMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return detallesActualizadosDesdeServidor;
    }
}
