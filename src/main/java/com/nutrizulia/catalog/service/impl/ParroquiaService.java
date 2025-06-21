package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ParroquiaDto;
import com.nutrizulia.catalog.mapper.ParroquiaMapper;
import com.nutrizulia.catalog.repository.MunicipioRepository;
import com.nutrizulia.catalog.repository.ParroquiaRepository;
import com.nutrizulia.catalog.service.IParroquiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParroquiaService implements IParroquiaService {

    private final ParroquiaRepository parroquiaRepository;
    private final ParroquiaMapper parroquiaMapper;
    private final MunicipioRepository municipioRepository;

    @Override
    public List<ParroquiaDto> getParroquias(Integer idMunicipio) {
        if (!municipioRepository.existsById(idMunicipio)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Municipio no encontrado con ID: '%s'", idMunicipio));
        }

        return parroquiaRepository.findAllByMunicipio_Id(idMunicipio)
                .stream()
                .map(parroquiaMapper::toDto)
                .collect(Collectors.toList());
    }

}
