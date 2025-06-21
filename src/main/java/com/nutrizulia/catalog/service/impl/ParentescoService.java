package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.ParentescoDto;
import com.nutrizulia.catalog.mapper.ParentescoMapper;
import com.nutrizulia.catalog.repository.ParentescoRepository;
import com.nutrizulia.catalog.service.IParentescoService;
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
