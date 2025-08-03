package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.TipoIndicadorDto;
import com.nutrizulia.features.catalog.mapper.TipoIndicadorMapper;
import com.nutrizulia.features.catalog.repository.TipoIndicadorRepository;
import com.nutrizulia.features.catalog.service.ITipoIndicadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoIndicadorService implements ITipoIndicadorService {

    private final TipoIndicadorRepository tipoIndicadorRepository;
    private final TipoIndicadorMapper tipoIndicadorMapper;

    @Override
    public List<TipoIndicadorDto> getTiposIndicadores() {
        return tipoIndicadorRepository.findAll()
                .stream()
                .map(tipoIndicadorMapper::toDto)
                .collect(Collectors.toList());
    }
}
