package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.MunicipioSanitarioDto;
import com.nutrizulia.catalog.mapper.MunicipioSanitarioMapper;
import com.nutrizulia.catalog.repository.EstadoRepository;
import com.nutrizulia.catalog.repository.MunicipioSanitarioRepository;
import com.nutrizulia.catalog.service.IMunicipioSanitarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipioSanitarioService implements IMunicipioSanitarioService {

    private final MunicipioSanitarioRepository municipioSanitarioRepository;
    private final MunicipioSanitarioMapper municipioSanitarioMapper;
    private final EstadoRepository estadoRepository;

    @Override
    public List<MunicipioSanitarioDto> getMunicipiosSanitarios(Integer idEstado) {
        if (!estadoRepository.existsById(idEstado)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Estado no encontrado con ID: '%s'", idEstado));
        }

        return municipioSanitarioRepository.findAllByEstado_Id(idEstado)
                .stream()
                .map(municipioSanitarioMapper::toDto)
                .collect(Collectors.toList());
    }

}
