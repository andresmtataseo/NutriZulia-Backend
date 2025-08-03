package com.nutrizulia.features.institution.service;

import com.nutrizulia.features.institution.dto.InstitucionDto;
import com.nutrizulia.features.institution.mapper.InstitucionMapper;
import com.nutrizulia.features.institution.repository.InstitucionRepository;
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

    @Override
    public InstitucionDto getInstitucionesById(Integer id) {
        return institucionRepository.findById(id).map(institucionMapper::toDto).orElse(null);
    }
}
