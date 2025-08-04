package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.DiagnosticoDto;

import java.util.List;

public interface IDiagnosticoService {
    List<DiagnosticoDto> syncDiagnosticos(List<DiagnosticoDto> diagnosticos);
}
