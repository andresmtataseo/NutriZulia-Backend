package com.nutrizulia.catalog.service.impl;

import com.nutrizulia.catalog.dto.RolDto;
import com.nutrizulia.catalog.mapper.RolMapper;
import com.nutrizulia.catalog.repository.RolRepository;
import com.nutrizulia.catalog.service.IRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RolService implements IRolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    public List<RolDto> getRoles() {
        return rolRepository.findAll().stream().map(rolMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public RolDto getRolById(Integer id) {
        return rolRepository.findById(id).map(rolMapper::toDto).orElse(null);
    }
}
