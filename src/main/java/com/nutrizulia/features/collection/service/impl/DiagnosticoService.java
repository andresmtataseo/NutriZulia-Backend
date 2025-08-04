package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.DiagnosticoDto;
import com.nutrizulia.features.collection.mapper.DiagnosticoMapper;
import com.nutrizulia.features.collection.repository.DiagnosticoRepository;
import com.nutrizulia.features.collection.service.IDiagnosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiagnosticoService implements IDiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final DiagnosticoMapper diagnosticoMapper;

    @Override
    public List<DiagnosticoDto> syncDiagnosticos(List<DiagnosticoDto> diagnosticos) {
        return List.of();
    }
}
