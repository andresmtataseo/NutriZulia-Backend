package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.ParametroCrecimientoPediatricoEdadDto;
import com.nutrizulia.features.catalog.mapper.ParametroCrecimientoPediatricoEdadMapper;
import com.nutrizulia.features.catalog.repository.ParametroCrecimientoPediatricoEdadRepository;
import com.nutrizulia.features.catalog.service.IParametroCrecimientoPediatricoEdadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParametroCrecimientoPediatricoEdadService implements IParametroCrecimientoPediatricoEdadService {

    private final ParametroCrecimientoPediatricoEdadRepository parametroCrecimientoPediatricoEdadRepository;
    private final ParametroCrecimientoPediatricoEdadMapper parametroCrecimientoPediatricoEdadMapper;

    @Override
    public List<ParametroCrecimientoPediatricoEdadDto> getParametrosCrecimientosPedriaticosEdad() {
        return parametroCrecimientoPediatricoEdadRepository.findAll()
                .stream()
                .map(parametroCrecimientoPediatricoEdadMapper::toDto)
                .collect(Collectors.toList());
    }
}
