package com.nutrizulia.service.impl;

import com.nutrizulia.dto.pre.EtniaDto;
import com.nutrizulia.mapper.EtniaMapper;
import com.nutrizulia.repository.EtniaRepository;
import com.nutrizulia.service.IEtniaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtniaService implements IEtniaService {

    private final EtniaRepository etniaRepository;
    private final EtniaMapper etniaMapper;

    public List<EtniaDto> getEtnias() {
        return etniaRepository.findAll()
                .stream()
                .map(etniaMapper::toDto)
                .collect(Collectors.toList());
    }
}
