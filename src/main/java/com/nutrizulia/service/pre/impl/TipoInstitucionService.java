package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.TipoInstitucionDto;
import com.nutrizulia.mapper.pre.TipoInstitucionMapper;
import com.nutrizulia.repository.pre.TipoInstitucionRepository;
import com.nutrizulia.service.pre.ITipoInstitucionService;
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
