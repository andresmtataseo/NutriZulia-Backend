package com.nutrizulia.features.catalog.service;

import com.nutrizulia.features.catalog.dto.VersionDto;

import java.util.List;

public interface IVersionService {
    List<VersionDto> getAll();
}
