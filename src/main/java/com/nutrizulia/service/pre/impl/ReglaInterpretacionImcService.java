package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.ReglaInterpretacionImcDto;
import com.nutrizulia.mapper.pre.ReglaInterpretacionImcMapper;
import com.nutrizulia.repository.pre.ReglaInterpretacionImcRepository;
import com.nutrizulia.service.pre.IReglaInterpretacionImcService;
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
