package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.GrupoEtarioDto;
import com.nutrizulia.features.catalog.mapper.GrupoEtarioMapper;
import com.nutrizulia.features.catalog.repository.GrupoEtarioRepository;
import com.nutrizulia.features.catalog.service.IGrupoEtarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrupoEtarioService implements IGrupoEtarioService {

    private final GrupoEtarioRepository grupoEtarioRepository;
    private final GrupoEtarioMapper grupoEtarioMapper;

    @Override
    public List<GrupoEtarioDto> getGruposEtarios() {
        return grupoEtarioRepository.findAll()
                .stream()
                .map(grupoEtarioMapper::toDto)
                .collect(Collectors.toList());
    }
}
