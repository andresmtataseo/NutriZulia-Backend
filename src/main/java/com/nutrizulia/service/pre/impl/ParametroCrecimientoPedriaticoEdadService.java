package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.ParametroCrecimientoPedriaticoEdadDto;
import com.nutrizulia.mapper.pre.ParametroCrecimientoPedriaticoEdadMapper;
import com.nutrizulia.repository.pre.ParametroCrecimientoPedriaticoEdadRepository;
import com.nutrizulia.service.pre.IParametroCrecimientoPedriaticoEdadService;
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
