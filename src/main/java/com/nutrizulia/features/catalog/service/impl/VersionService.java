package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.VersionDto;
import com.nutrizulia.features.catalog.mapper.VersionMapper;
import com.nutrizulia.features.catalog.repository.VersionRepository;
import com.nutrizulia.features.catalog.service.IVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VersionService implements IVersionService {

    private final VersionRepository versionRepository;
    private final VersionMapper versionMapper;

    @Override
    public List<VersionDto> getAll() {
        return versionRepository.findAll().stream().map(versionMapper::toDto).collect(Collectors.toList());
    }
}
