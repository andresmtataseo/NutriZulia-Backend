package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ParametroCrecimientoPediatricoLongitudDto;
import com.nutrizulia.catalog.mapper.ParametroCrecimientoPediatricoLongitudMapper;
import com.nutrizulia.catalog.repository.ParametroCrecimientoPediatricoLongitudRepository;
import com.nutrizulia.catalog.service.IParametroCrecimientoPediatricoLongitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParametroCrecimientoPediatricoLongitudService implements IParametroCrecimientoPediatricoLongitudService {

    private final ParametroCrecimientoPediatricoLongitudRepository parametroCrecimientoPediatricoLongitudRepository;
    private final ParametroCrecimientoPediatricoLongitudMapper parametroCrecimientoPediatricoLongitudMapper;

    @Override
    public List<ParametroCrecimientoPediatricoLongitudDto> getParametrosCrecimientosPedriaticosLongitud() {
        return parametroCrecimientoPediatricoLongitudRepository.findAll()
                .stream()
                .map(parametroCrecimientoPediatricoLongitudMapper::toDto)
                .collect(Collectors.toList());
    }
}
