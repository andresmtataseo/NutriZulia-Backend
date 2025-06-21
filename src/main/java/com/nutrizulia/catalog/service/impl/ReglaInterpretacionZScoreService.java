package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ReglaInterpretacionZScoreDto;
import com.nutrizulia.catalog.mapper.ReglaInterpretacionZScoreMapper;
import com.nutrizulia.catalog.repository.ReglaInterpretacionZScoreRepository;
import com.nutrizulia.catalog.service.IReglaInterpretacionZScoreService;
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
