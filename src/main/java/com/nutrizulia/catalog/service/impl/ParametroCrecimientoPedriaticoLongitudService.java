package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ParametroCrecimientoPedriaticoLongitudDto;
import com.nutrizulia.catalog.mapper.ParametroCrecimientoPedriaticoLongitudMapper;
import com.nutrizulia.catalog.repository.ParametroCrecimientoPedriaticoLongitudRepository;
import com.nutrizulia.catalog.service.IParametroCrecimientoPedriaticoLongitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParametroCrecimientoPedriaticoLongitudService implements IParametroCrecimientoPedriaticoLongitudService {

    private final ParametroCrecimientoPedriaticoLongitudRepository parametroCrecimientoPedriaticoLongitudRepository;
    private final ParametroCrecimientoPedriaticoLongitudMapper parametroCrecimientoPedriaticoLongitudMapper;

    @Override
    public List<ParametroCrecimientoPedriaticoLongitudDto> getParametrosCrecimientosPedriaticosLongitud() {
        return parametroCrecimientoPedriaticoLongitudRepository.findAll()
                .stream()
                .map(parametroCrecimientoPedriaticoLongitudMapper::toDto)
                .collect(Collectors.toList());
    }
}
