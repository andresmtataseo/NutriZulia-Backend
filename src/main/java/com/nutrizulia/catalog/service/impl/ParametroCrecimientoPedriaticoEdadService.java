package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ParametroCrecimientoPedriaticoEdadDto;
import com.nutrizulia.catalog.mapper.ParametroCrecimientoPedriaticoEdadMapper;
import com.nutrizulia.catalog.repository.ParametroCrecimientoPedriaticoEdadRepository;
import com.nutrizulia.catalog.service.IParametroCrecimientoPedriaticoEdadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParametroCrecimientoPedriaticoEdadService implements IParametroCrecimientoPedriaticoEdadService {

    private final ParametroCrecimientoPedriaticoEdadRepository parametroCrecimientoPedriaticoEdadRepository;
    private final ParametroCrecimientoPedriaticoEdadMapper parametroCrecimientoPedriaticoEdadMapper;

    @Override
    public List<ParametroCrecimientoPedriaticoEdadDto> getParametrosCrecimientosPedriaticosEdad() {
        return parametroCrecimientoPedriaticoEdadRepository.findAll()
                .stream()
                .map(parametroCrecimientoPedriaticoEdadMapper::toDto)
                .collect(Collectors.toList());
    }
}
