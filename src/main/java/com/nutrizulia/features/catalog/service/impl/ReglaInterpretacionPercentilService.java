package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.ReglaInterpretacionPercentilDto;
import com.nutrizulia.features.catalog.mapper.ReglaInterpretacionPercentilMapper;
import com.nutrizulia.features.catalog.repository.ReglaInterpretacionPercentilRepository;
import com.nutrizulia.features.catalog.service.IReglaInterpretacionPercentilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReglaInterpretacionPercentilService implements IReglaInterpretacionPercentilService {

    private final ReglaInterpretacionPercentilRepository reglaInterpretacionPercentilRepository;
    private final ReglaInterpretacionPercentilMapper reglaInterpretacionPercentilMapper;

    @Override
    public List<ReglaInterpretacionPercentilDto> getReglasInterpretacionesPercentil() {
        return reglaInterpretacionPercentilRepository.findAll()
                .stream()
                .map(reglaInterpretacionPercentilMapper::toDto)
                .collect(Collectors.toList());
    }
}
