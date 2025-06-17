package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.RiesgoBiologicoDto;
import com.nutrizulia.mapper.pre.RiesgoBiologicoMapper;
import com.nutrizulia.repository.pre.RiesgoBiologicoRepository;
import com.nutrizulia.service.pre.IRiesgoBiologicoService;
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
