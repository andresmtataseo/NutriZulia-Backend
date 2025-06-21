package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.RiesgoBiologicoDto;
import com.nutrizulia.catalog.mapper.RiesgoBiologicoMapper;
import com.nutrizulia.catalog.repository.RiesgoBiologicoRepository;
import com.nutrizulia.catalog.service.IRiesgoBiologicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RiesgoBiologicoService implements IRiesgoBiologicoService {

    private final RiesgoBiologicoRepository riesgoBiologicoRepository;
    private final RiesgoBiologicoMapper risgoBiologicoMapper;

    @Override
    public List<RiesgoBiologicoDto> getRiesgosBiologicos() {
        return riesgoBiologicoRepository.findAll()
                .stream()
                .map(risgoBiologicoMapper::toDto)
                .collect(Collectors.toList());
    }
}
