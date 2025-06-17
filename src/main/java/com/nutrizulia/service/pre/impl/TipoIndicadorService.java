package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.TipoIndicadorDto;
import com.nutrizulia.mapper.pre.TipoIndicadorMapper;
import com.nutrizulia.repository.pre.TipoIndicadorRepository;
import com.nutrizulia.service.pre.ITipoIndicadorService;
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
