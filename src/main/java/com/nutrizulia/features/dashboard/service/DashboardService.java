package com.nutrizulia.features.dashboard.service;

import com.nutrizulia.features.dashboard.dto.ChartResponseDto;

import java.time.YearMonth;

public interface DashboardService {

    ChartResponseDto getConsultasPorMes(YearMonth inicio, YearMonth fin, Integer municipioId, Integer institucionId);

    // Simplificado: conteo actual, opcional filtro por municipio sanitario
    ChartResponseDto getInstitucionesActivasPorMunicipio(Integer municipioId);

    // Simplificado: conteo actual, opcional filtro por municipio sanitario
    ChartResponseDto getUsuariosActivosPorInstitucion(Integer municipioId);

    ChartResponseDto getDistribucionGrupoEtario(YearMonth inicio, YearMonth fin, Integer institucionId, Integer municipioId);

    ChartResponseDto getEstadoNutricionalPorGrupoEtario(YearMonth inicio, YearMonth fin, Integer institucionId, Integer municipioId);
}