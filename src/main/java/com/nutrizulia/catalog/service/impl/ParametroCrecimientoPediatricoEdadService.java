package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ParametroCrecimientoPediatricoEdadDto;
import com.nutrizulia.catalog.mapper.ParametroCrecimientoPediatricoEdadMapper;
import com.nutrizulia.catalog.repository.ParametroCrecimientoPediatricoEdadRepository;
import com.nutrizulia.catalog.service.IParametroCrecimientoPediatricoEdadService;
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
