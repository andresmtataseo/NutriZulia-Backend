package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.InstitucionDto;
import com.nutrizulia.mapper.pre.InstitucionMapper;
import com.nutrizulia.repository.pre.InstitucionRepository;
import com.nutrizulia.service.pre.IInstitucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InstitucionService implements IInstitucionService {

    private final InstitucionRepository institucionRepository;
    private final InstitucionMapper institucionMapper;

    @Override
    public List<InstitucionDto> getInstituciones() {
        return institucionRepository.findAll().stream().map(institucionMapper::toDto).collect(Collectors.toList());
    }
}
