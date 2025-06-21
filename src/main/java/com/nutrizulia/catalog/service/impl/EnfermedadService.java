package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.EnfermedadDto;
import com.nutrizulia.catalog.mapper.EnfermedadMapper;
import com.nutrizulia.catalog.repository.EnfermedadRepository;
import com.nutrizulia.catalog.service.IEnfermedadService;
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
