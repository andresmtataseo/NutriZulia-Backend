package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.ReglaInterpretacionImcDto;
import com.nutrizulia.features.catalog.mapper.ReglaInterpretacionImcMapper;
import com.nutrizulia.features.catalog.repository.ReglaInterpretacionImcRepository;
import com.nutrizulia.features.catalog.service.IReglaInterpretacionImcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReglaInterpretacionImcService implements IReglaInterpretacionImcService {

    private final ReglaInterpretacionImcRepository reglaInterpretacionImcRepository;
    private final ReglaInterpretacionImcMapper reglaInterpretacionImcMapper;

    @Override
    public List<ReglaInterpretacionImcDto> getReglasInterpretacionesImc() {
        return reglaInterpretacionImcRepository.findAll()
                .stream()
                .map(reglaInterpretacionImcMapper::toDto)
                .collect(Collectors.toList());
    }
}
