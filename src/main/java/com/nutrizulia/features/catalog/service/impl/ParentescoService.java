package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.ParentescoDto;
import com.nutrizulia.features.catalog.mapper.ParentescoMapper;
import com.nutrizulia.features.catalog.repository.ParentescoRepository;
import com.nutrizulia.features.catalog.service.IParentescoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParentescoService implements IParentescoService {

    private final ParentescoRepository parentescoRepository;
    private final ParentescoMapper parentescoMapper;

    @Override
    public List<ParentescoDto> getParentescos() {
        return parentescoRepository.findAll()
                .stream()
                .map(parentescoMapper::toDto)
                .collect(Collectors.toList());
    }
}
