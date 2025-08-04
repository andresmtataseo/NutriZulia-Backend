package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.mapper.ActividadMapper;
import com.nutrizulia.features.collection.model.Actividad;
import com.nutrizulia.features.collection.repository.ActividadRepository;
import com.nutrizulia.features.collection.service.IActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActividadService implements IActividadService {

    private final ActividadRepository actividadRepository;
    private final ActividadMapper actividadMapper;

    @Override
    public List<ActividadDto> getActividades() {
        return actividadRepository.findAll().stream().map(actividadMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ActividadDto> sycnActividades(List<ActividadDto> actividades) {
        List<ActividadDto> actividadesActualizadasDesdeServidor = new ArrayList<>();

        for (ActividadDto dto : actividades) {
            Optional<Actividad> existenteOpt = actividadRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                Actividad nueva = actividadMapper.toEntity(dto);
                actividadRepository.save(nueva);
            } else {
                Actividad existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    Actividad actualizada = actividadMapper.toEntity(dto);
                    actividadRepository.save(actualizada);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    actividadesActualizadasDesdeServidor.add(actividadMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return actividadesActualizadasDesdeServidor;
    }
}
