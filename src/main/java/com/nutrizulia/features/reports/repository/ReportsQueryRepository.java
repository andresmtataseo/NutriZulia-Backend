package com.nutrizulia.features.reports.repository;

import java.time.LocalDate;
import java.util.List;

public interface ReportsQueryRepository {

    List<Object[]> obtenerResumenConsultasPorEdadPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
    // Nuevo: resumen de actividades por tipo para una institución y rango de fechas
    List<Object[]> obtenerResumenActividadesPorTipoPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
    // Nuevo: resumen de evaluación antropométrica Peso/Edad (tipo_indicador_id=4) en menores de 2 años por institución y rango de fechas
    List<Object[]> obtenerResumenAntropometriaPesoEdadMenores2PorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
    List<Object[]> obtenerResumenAntropometriaPesoEdadMenores2ConsultasRegularesPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
    List<Object[]> obtenerResumenAntropometriaCombinadaNinos2a9PorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
}