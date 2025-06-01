package com.nutrizulia.service.pre.impl;

import com.nutrizulia.dto.pre.RolDto;
import com.nutrizulia.mapper.pre.RolMapper;
import com.nutrizulia.repository.pre.RolRepository;
import com.nutrizulia.service.pre.IRolService;
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
}
