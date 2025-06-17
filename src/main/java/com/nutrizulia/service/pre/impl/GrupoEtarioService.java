package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.GrupoEtarioDto;
import com.nutrizulia.mapper.pre.GrupoEtarioMapper;
import com.nutrizulia.repository.pre.GrupoEtarioRepository;
import com.nutrizulia.service.pre.IGrupoEtarioService;
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
