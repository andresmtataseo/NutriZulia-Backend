package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.EnfermedadDto;
import com.nutrizulia.features.catalog.mapper.EnfermedadMapper;
import com.nutrizulia.features.catalog.repository.EnfermedadRepository;
import com.nutrizulia.features.catalog.service.IEnfermedadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EnfermedadService implements IEnfermedadService {

    private final EnfermedadRepository enfermedadRepository;
    private final EnfermedadMapper enfermedadMapper;

    @Override
    public List<EnfermedadDto> getEnfermedades() {
        return enfermedadRepository.findAll().stream().map(enfermedadMapper::toDto).collect(Collectors.toList());
    }
}
