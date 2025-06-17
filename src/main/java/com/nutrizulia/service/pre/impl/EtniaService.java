package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.EtniaDto;
import com.nutrizulia.mapper.pre.EtniaMapper;
import com.nutrizulia.repository.pre.EtniaRepository;
import com.nutrizulia.service.pre.IEtniaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtniaService implements IEtniaService {

    private final EtniaRepository etniaRepository;
    private final EtniaMapper etniaMapper;

    @Override
    public List<EtniaDto> getEtnias() {
        return etniaRepository.findAll()
                .stream()
                .map(etniaMapper::toDto)
                .collect(Collectors.toList());
    }
}
