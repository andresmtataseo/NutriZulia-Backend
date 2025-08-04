package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleVitalDto;
import com.nutrizulia.features.collection.mapper.DetalleVitalMapper;
import com.nutrizulia.features.collection.model.DetalleVital;
import com.nutrizulia.features.collection.repository.DetalleVitalRepository;
import com.nutrizulia.features.collection.service.IDetalleVitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DetalleVitalService implements IDetalleVitalService {

    private final DetalleVitalRepository detalleVitalRepository;
    private final DetalleVitalMapper detalleVitalMapper;

    @Override
    public List<DetalleVitalDto> syncDetallesVital(List<DetalleVitalDto> detallesVitales) {
        List<DetalleVitalDto> detallesActualizadosDesdeServidor = new ArrayList<>();

        for (DetalleVitalDto dto : detallesVitales) {
            Optional<DetalleVital> existenteOpt = detalleVitalRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                DetalleVital nuevo = detalleVitalMapper.toEntity(dto);
                detalleVitalRepository.save(nuevo);
            } else {
                DetalleVital existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    DetalleVital actualizado = detalleVitalMapper.toEntity(dto);
                    detalleVitalRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    detallesActualizadosDesdeServidor.add(detalleVitalMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return detallesActualizadosDesdeServidor;
    }
}
