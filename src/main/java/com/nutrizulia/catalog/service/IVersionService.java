package com.nutrizulia.catalog.service;

import com.nutrizulia.catalog.dto.VersionDto;

import java.util.List;

public interface IVersionService {
    List<VersionDto> getAll();
}
