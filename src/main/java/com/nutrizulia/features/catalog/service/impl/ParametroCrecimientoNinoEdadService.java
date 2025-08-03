package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.ParametroCrecimientoNinoEdadDto;
import com.nutrizulia.features.catalog.mapper.ParametroCrecimientoNinoEdadMapper;
import com.nutrizulia.features.catalog.repository.ParametroCrecimientoNinoEdadRepository;
import com.nutrizulia.features.catalog.service.IParametroCrecimientoNinoEdadService;
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
