package com.nutrizulia.features.dashboard.repository;

import java.util.List;

public interface DashboardQueryRepository {

    List<Object[]> consultasPorMes(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin, Integer municipioId, Integer institucionId);

    List<Object[]> institucionesActivasPorMunicipio(Integer municipioId);

    List<Object[]> usuariosActivosPorInstitucion(Integer municipioId);

    List<Object[]> distribucionGrupoEtario(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin, Integer institucionId, Integer municipioId);

    List<Object[]> estadoNutricionalPorGrupoEtario(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin, Integer institucionId, Integer municipioId);
}