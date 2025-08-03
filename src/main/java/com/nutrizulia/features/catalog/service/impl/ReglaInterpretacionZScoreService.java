package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.ReglaInterpretacionZScoreDto;
import com.nutrizulia.features.catalog.mapper.ReglaInterpretacionZScoreMapper;
import com.nutrizulia.features.catalog.repository.ReglaInterpretacionZScoreRepository;
import com.nutrizulia.features.catalog.service.IReglaInterpretacionZScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReglaInterpretacionZScoreService implements IReglaInterpretacionZScoreService {

    private final ReglaInterpretacionZScoreRepository reglaInterpretacionZScoreRepository;
    private final ReglaInterpretacionZScoreMapper reglaInterpretacionZScoreMapper;

    @Override
    public List<ReglaInterpretacionZScoreDto> getReglasInterpretacionesZScore() {
        return reglaInterpretacionZScoreRepository.findAll()
                .stream()
                .map(reglaInterpretacionZScoreMapper::toDto)
                .collect(Collectors.toList());
    }
}
