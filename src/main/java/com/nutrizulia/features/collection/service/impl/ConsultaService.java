package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.ConsultaDto;
import com.nutrizulia.features.collection.mapper.ConsultaMapper;
import com.nutrizulia.features.collection.model.Consulta;
import com.nutrizulia.features.collection.repository.ConsultaRepository;
import com.nutrizulia.features.collection.service.IConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConsultaService implements IConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;

    @Override
    public List<ConsultaDto> syncConsultas(List<ConsultaDto> consultas) {
        List<ConsultaDto> consultasActualizadasDesdeServidor = new ArrayList<>();

        for (ConsultaDto dto : consultas) {
            Optional<Consulta> existenteOpt = consultaRepository.findById(dto.getId());

            if (existenteOpt.isEmpty()) {
                Consulta nueva = consultaMapper.toEntity(dto);
                consultaRepository.save(nueva);
            } else {
                Consulta existente = existenteOpt.get();
                if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                    Consulta actualizada = consultaMapper.toEntity(dto);
                    consultaRepository.save(actualizada);
                } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                    // El servidor tiene la versión más reciente
                    consultasActualizadasDesdeServidor.add(consultaMapper.toDto(existente));
                }
                // Si son iguales, no se hace nada
            }
        }

        return consultasActualizadasDesdeServidor;
    }
}
