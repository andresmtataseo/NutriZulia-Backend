package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.TipoInstitucionDto;
import com.nutrizulia.catalog.mapper.TipoInstitucionMapper;
import com.nutrizulia.catalog.repository.TipoInstitucionRepository;
import com.nutrizulia.catalog.service.ITipoInstitucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoInstitucionService implements ITipoInstitucionService {

    private final TipoInstitucionRepository tipoInstitucionRepository;
    private final TipoInstitucionMapper tipoInstitucionMapper;

    @Override
    public List<TipoInstitucionDto> getTiposInstituciones() {
        return tipoInstitucionRepository.findAll().stream().map(tipoInstitucionMapper::toDto).collect(Collectors.toList());
    }
}
