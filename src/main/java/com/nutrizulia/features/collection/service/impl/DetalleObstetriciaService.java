package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleObstetriciaDto;
import com.nutrizulia.features.collection.mapper.DetalleObstetriciaMapper;
import com.nutrizulia.features.collection.model.DetalleObstetricia;
import com.nutrizulia.features.collection.repository.DetalleObstetriciaRepository;
import com.nutrizulia.features.collection.service.IDetalleObstetriciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DetalleObstetriciaService implements IDetalleObstetriciaService {

    private final DetalleObstetriciaRepository detalleObstetriciaRepository;
    private final DetalleObstetriciaMapper detalleObstetriciaMapper;

    @Override
    public List<DetalleObstetriciaDto> syncDetallesObstetricia(List<DetalleObstetriciaDto> detallesObstetricias) {
        List<DetalleObstetriciaDto> detallesActualizadosDesdeServidor = new ArrayList<>();

        for (DetalleObstetriciaDto dto : detallesObstetricias) {
            Optional<DetalleObstetricia> existenteOpt = detalleObstetriciaRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                DetalleObstetricia nuevo = detalleObstetriciaMapper.toEntity(dto);
                detalleObstetriciaRepository.save(nuevo);
            } else {
                DetalleObstetricia existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    DetalleObstetricia actualizado = detalleObstetriciaMapper.toEntity(dto);
                    detalleObstetriciaRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    detallesActualizadosDesdeServidor.add(detalleObstetriciaMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return detallesActualizadosDesdeServidor;
    }
}
