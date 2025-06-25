package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.RegexDto;
import com.nutrizulia.catalog.mapper.RegexMapper;
import com.nutrizulia.catalog.repository.RegexRepository;
import com.nutrizulia.catalog.service.IRegexService;
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
