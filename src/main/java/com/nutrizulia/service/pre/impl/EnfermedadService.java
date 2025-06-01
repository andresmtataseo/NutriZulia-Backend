package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.EnfermedadDto;
import com.nutrizulia.mapper.pre.EnfermedadMapper;
import com.nutrizulia.repository.pre.EnfermedadRepository;
import com.nutrizulia.service.pre.IEnfermedadService;
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
