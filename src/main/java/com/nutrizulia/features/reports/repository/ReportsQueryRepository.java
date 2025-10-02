package com.nutrizulia.features.reports.repository;

import java.time.LocalDate;
import java.util.List;

public interface ReportsQueryRepository {

    List<Object[]> obtenerResumenConsultasPorEdadPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
}