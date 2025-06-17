package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.ReglaInterpretacionPercentilDto;
import com.nutrizulia.mapper.pre.ReglaInterpretacionPercentilMapper;
import com.nutrizulia.repository.pre.ReglaInterpretacionPercentilRepository;
import com.nutrizulia.service.pre.IReglaInterpretacionPercentilService;
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
