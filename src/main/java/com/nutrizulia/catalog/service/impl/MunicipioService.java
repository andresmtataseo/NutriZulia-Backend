package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.MunicipioDto;
import com.nutrizulia.catalog.mapper.MunicipioMapper;
import com.nutrizulia.catalog.repository.EstadoRepository;
import com.nutrizulia.catalog.repository.MunicipioRepository;
import com.nutrizulia.catalog.service.IMunicipioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipioService implements IMunicipioService {

    private final MunicipioRepository municipioRepository;
    private final MunicipioMapper municipioMapper;
    private final EstadoRepository estadoRepository;

    @Override
    public List<MunicipioDto> getMunicipios(Integer idEstado) {
        if (!estadoRepository.existsById(idEstado)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Estado no encontrado con ID: '%s'", idEstado));
        }

        return municipioRepository.findAllByEstado_Id(idEstado)
                .stream()
                .map(municipioMapper::toDto)
                .collect(Collectors.toList());
    }
}