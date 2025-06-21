package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ParametroCrecimientoNinoEdadDto;
import com.nutrizulia.catalog.mapper.ParametroCrecimientoNinoEdadMapper;
import com.nutrizulia.catalog.repository.ParametroCrecimientoNinoEdadRepository;
import com.nutrizulia.catalog.service.IParametroCrecimientoNinoEdadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParametroCrecimientoNinoEdadService implements IParametroCrecimientoNinoEdadService {

    private final ParametroCrecimientoNinoEdadRepository parametroCrecimientoNinoEdadRepository;
    private final ParametroCrecimientoNinoEdadMapper parametroCrecimientoNinoEdadMapper;

    @Override
    public List<ParametroCrecimientoNinoEdadDto> getParametrosCrecimientosNinosEdad() {
        return parametroCrecimientoNinoEdadRepository.findAll()
                .stream()
                .map(parametroCrecimientoNinoEdadMapper::toDto)
                .collect(Collectors.toList());
    }
}
