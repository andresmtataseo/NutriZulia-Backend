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
    List<Object[]> obtenerResumenAntropometriaCombinadaNinos2a9ConsultasRegularesPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
    // Nuevo: resumen combinado IMC/Edad (id=1) y Altura/Edad (id=7) para 10-18 años 11 meses, tipo_actividad_id=10
    List<Object[]> obtenerResumenAntropometriaCombinadaNinos10a18PorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);
    // Nuevo: variante para consultas regulares (tipo_actividad_id=1) en 10-18 años 11 meses
    List<Object[]> obtenerResumenAntropometriaCombinadaNinos10a18ConsultasRegularesPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);

    // Nuevo: resumen IMC adultos (tipo_indicador_id=8) para mayores de 19 años, desglosado por grupo de edad (19-59 y 60+)
    List<Object[]> obtenerResumenImcAdultosPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);

    // Nuevo: resumen de riesgos biológicos por institución considerando solo PRIMERA_CONSULTA y diagnóstico principal
    List<Object[]> obtenerResumenRiesgoBiologicoPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId);

    // Nuevo: última actualización (MAX(updated_at)) por usuario_institucion_id para un conjunto de IDs
    List<Object[]> obtenerUltimaActualizacionPorUsuarioInstitucionIds(List<Integer> usuarioInstitucionIds);
}