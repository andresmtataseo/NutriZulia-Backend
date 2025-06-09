package com.nutrizulia.service;

import com.nutrizulia.dto.InstitucionDto;
import com.nutrizulia.mapper.InstitucionMapper;
import com.nutrizulia.repository.InstitucionRepository;
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
