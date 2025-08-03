package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.RegexDto;
import com.nutrizulia.features.catalog.mapper.RegexMapper;
import com.nutrizulia.features.catalog.repository.RegexRepository;
import com.nutrizulia.features.catalog.service.IRegexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegexService implements IRegexService {

    private final RegexRepository regexRepository;
    private final RegexMapper regexMapper;

    @Override
    public List<RegexDto> getAll() {
        return regexRepository.findAll().stream().map(regexMapper::toDto).collect(Collectors.toList());
    }
}
