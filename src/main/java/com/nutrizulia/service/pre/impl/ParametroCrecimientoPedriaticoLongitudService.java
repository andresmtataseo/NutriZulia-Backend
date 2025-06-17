package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.ParametroCrecimientoPedriaticoLongitudDto;
import com.nutrizulia.mapper.pre.ParametroCrecimientoPedriaticoLongitudMapper;
import com.nutrizulia.repository.pre.ParametroCrecimientoPedriaticoLongitudRepository;
import com.nutrizulia.service.pre.IParametroCrecimientoPedriaticoLongitudService;
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
