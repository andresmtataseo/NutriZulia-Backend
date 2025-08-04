package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.RepresentanteDto;
import com.nutrizulia.features.collection.mapper.RepresentanteMapper;
import com.nutrizulia.features.collection.repository.RepresentanteRepository;
import com.nutrizulia.features.collection.service.IRepresentanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RepresentanteService implements IRepresentanteService {

    private final RepresentanteRepository representanteRepository;
    private final RepresentanteMapper representanteMapper;

    @Override
    public List<RepresentanteDto> syncRepresentantes(List<RepresentanteDto> representantes) {
        return List.of();
    }
}
