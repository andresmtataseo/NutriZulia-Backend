package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ReglaInterpretacionPercentilDto;
import com.nutrizulia.catalog.mapper.ReglaInterpretacionPercentilMapper;
import com.nutrizulia.catalog.repository.ReglaInterpretacionPercentilRepository;
import com.nutrizulia.catalog.service.IReglaInterpretacionPercentilService;
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
