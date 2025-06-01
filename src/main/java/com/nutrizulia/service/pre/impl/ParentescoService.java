package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.ParentescoDto;
import com.nutrizulia.mapper.pre.ParentescoMapper;
import com.nutrizulia.repository.pre.ParentescoRepository;
import com.nutrizulia.service.pre.IParentescoService;
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
