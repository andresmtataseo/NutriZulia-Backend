package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.ReglaInterpretacionZScoreDto;
import com.nutrizulia.mapper.pre.ReglaInterpretacionZScoreMapper;
import com.nutrizulia.repository.pre.ReglaInterpretacionZScoreRepository;
import com.nutrizulia.service.pre.IReglaInterpretacionZScoreService;
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
