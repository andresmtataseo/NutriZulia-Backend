package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;
import com.nutrizulia.features.collection.mapper.DetalleMetabolicoMapper;
import com.nutrizulia.features.collection.model.DetalleMetabolico;
import com.nutrizulia.features.collection.repository.DetalleMetabolicoRepository;
import com.nutrizulia.features.collection.service.IDetalleMetabolicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DetalleMetabolicoService implements IDetalleMetabolicoService {

    private final DetalleMetabolicoRepository detalleMetabolicoRepository;
    private final DetalleMetabolicoMapper detalleMetabolicoMapper;

    @Override
    public List<DetalleMetabolicoDto> syncDetallesMetabolico(List<DetalleMetabolicoDto> detallesMetabolicos) {
        List<DetalleMetabolicoDto> detallesActualizadosDesdeServidor = new ArrayList<>();

        for (DetalleMetabolicoDto dto : detallesMetabolicos) {
            Optional<DetalleMetabolico> existenteOpt = detalleMetabolicoRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                DetalleMetabolico nuevo = detalleMetabolicoMapper.toEntity(dto);
                detalleMetabolicoRepository.save(nuevo);
            } else {
                DetalleMetabolico existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    DetalleMetabolico actualizado = detalleMetabolicoMapper.toEntity(dto);
                    detalleMetabolicoRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    detallesActualizadosDesdeServidor.add(detalleMetabolicoMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return detallesActualizadosDesdeServidor;
    }
}
