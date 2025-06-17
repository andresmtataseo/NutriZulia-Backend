package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.ParametroCrecimientoNinoEdadDto;
import com.nutrizulia.mapper.pre.ParametroCrecimientoNinoEdadMapper;
import com.nutrizulia.repository.pre.ParametroCrecimientoNinoEdadRepository;
import com.nutrizulia.service.pre.IParametroCrecimientoNinoEdadService;
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
