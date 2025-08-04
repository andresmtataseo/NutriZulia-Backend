package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.RepresentanteDto;
import com.nutrizulia.features.collection.mapper.RepresentanteMapper;
import com.nutrizulia.features.collection.model.Representante;
import com.nutrizulia.features.collection.repository.RepresentanteRepository;
import com.nutrizulia.features.collection.service.IRepresentanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RepresentanteService implements IRepresentanteService {

    private final RepresentanteRepository representanteRepository;
    private final RepresentanteMapper representanteMapper;

    @Override
    public List<RepresentanteDto> syncRepresentantes(List<RepresentanteDto> representantes) {
        List<RepresentanteDto> representantesActualizadosDesdeServidor = new ArrayList<>();

        for (RepresentanteDto dto : representantes) {
            Optional<Representante> existenteOpt = representanteRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                Representante nuevo = representanteMapper.toEntity(dto);
                representanteRepository.save(nuevo);
            } else {
                Representante existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    Representante actualizado = representanteMapper.toEntity(dto);
                    representanteRepository.save(actualizado);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    representantesActualizadosDesdeServidor.add(representanteMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return representantesActualizadosDesdeServidor;
    }
}
