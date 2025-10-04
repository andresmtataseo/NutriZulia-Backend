package com.nutrizulia.features.reports.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReportsQueryRepositoryImpl implements ReportsQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenConsultasPorEdadPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        String sql = """
            SELECT t.bucket AS rango,
                   SUM(CASE WHEN t.tipo_consulta = 'PRIMERA_CONSULTA' THEN 1 ELSE 0 END) AS total_primeras,
                   SUM(CASE WHEN t.tipo_consulta = 'CONSULTA_SUCESIVA' THEN 1 ELSE 0 END) AS total_sucesivas
            FROM (
                SELECT
                    CASE
                        WHEN d.esta_embarazada IS TRUE THEN 'PRENATAL'
                        ELSE CASE
                            WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int < 2 THEN 'LT2'
                            WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 2 AND 6 THEN 'A2_6'
                            WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 7 AND 12 THEN 'A7_12'
                            WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 12 AND 19 THEN 'A12_19'
                            WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 19 AND 60 THEN 'A19_60'
                            ELSE 'GTE60'
                        END
                    END AS bucket,
                    c.tipo_consulta
                FROM consultas c
                JOIN pacientes p ON p.id = c.paciente_id
                LEFT JOIN detalles_obstetricias d ON d.consulta_id = c.id AND d.is_deleted = FALSE
                JOIN usuarios_instituciones ui ON ui.id = c.usuario_institucion_id AND ui.is_enabled = TRUE
                WHERE c.is_deleted = FALSE
                  AND p.is_deleted = FALSE
                  AND c.estado IN ('COMPLETADA','SIN_PREVIA_CITA')
                  AND c.fecha_hora_real::date BETWEEN :fechaInicio AND :fechaFin
                  AND ui.institucion_id = :institucionId
            ) t
            GROUP BY t.bucket
            ORDER BY CASE t.bucket
                WHEN 'LT2' THEN 1
                WHEN 'A2_6' THEN 2
                WHEN 'A7_12' THEN 3
                WHEN 'A12_19' THEN 4
                WHEN 'A19_60' THEN 5
                WHEN 'GTE60' THEN 6
                WHEN 'PRENATAL' THEN 7
                ELSE 99 END
            """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("institucionId", institucionId)
                .getResultList();

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenActividadesPorTipoPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        String sql = """
            SELECT ta.id AS tipo_id,
                   ta.nombre AS tipo_nombre,
                   COALESCE(SUM(a.cantidad_participantes), 0) AS total_participantes,
                   COALESCE(SUM(COALESCE(a.cantidad_sesiones, 0)), 0) AS total_veces
            FROM actividades a
            JOIN usuarios_instituciones ui ON ui.id = a.usuario_institucion_id AND ui.is_enabled = TRUE
            JOIN tipos_actividades ta ON ta.id = a.tipo_actividad_id
            WHERE a.is_deleted = FALSE
              AND a.fecha BETWEEN :fechaInicio AND :fechaFin
              AND ui.institucion_id = :institucionId
            GROUP BY ta.id, ta.nombre
            ORDER BY ta.id
            """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("institucionId", institucionId)
                .getResultList();
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenAntropometriaPesoEdadMenores2PorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        String sql = """
            SELECT t.categoria,
                   COUNT(*) AS total
            FROM (
                SELECT
                    CASE
                        WHEN z.valor_calculado > 3 THEN 'Exceso Moderado y Grave'
                        WHEN (z.valor_calculado IS NULL OR z.valor_calculado <= 3) AND p.valor_calculado > 97 THEN 'Exceso Leve'
                        WHEN p.valor_calculado > 90 AND p.valor_calculado <= 97 THEN 'Riesgo de Exceso'
                        WHEN p.valor_calculado > 10 AND p.valor_calculado <= 90 THEN 'Normal'
                        WHEN p.valor_calculado > 3 AND p.valor_calculado <= 10 THEN 'Riesgo de Déficit'
                        WHEN p.valor_calculado <= 3 AND (z.valor_calculado IS NULL OR z.valor_calculado > -3) THEN 'Déficit Leve'
                        WHEN z.valor_calculado <= -3 AND z.valor_calculado > -4 THEN 'Déficit Moderado'
                        WHEN z.valor_calculado <= -4 THEN 'Déficit Grave'
                        ELSE 'Sin Clasificación'
                    END AS categoria
                FROM evaluaciones_antropometricas z
                LEFT JOIN evaluaciones_antropometricas p ON p.consulta_id = z.consulta_id
                    AND p.tipo_indicador_id = 4
                    AND p.tipo_valor_calculado = 'PERCENTIL'
                    AND p.is_deleted = FALSE
                JOIN consultas c ON c.id = z.consulta_id
                JOIN pacientes pa ON pa.id = c.paciente_id
                JOIN usuarios_instituciones ui ON ui.id = c.usuario_institucion_id AND ui.is_enabled = TRUE
                WHERE z.tipo_indicador_id = 4
                  AND z.tipo_valor_calculado = 'Z_SCORE'
                  AND z.is_deleted = FALSE
                  AND c.is_deleted = FALSE
                  AND pa.is_deleted = FALSE
                  AND c.estado IN ('COMPLETADA','SIN_PREVIA_CITA')
                  AND c.fecha_hora_real::date BETWEEN :fechaInicio AND :fechaFin
                  AND ui.institucion_id = :institucionId
                  AND EXTRACT(YEAR FROM age(c.fecha_hora_real::date, pa.fecha_nacimiento))::int < 2
                  AND c.tipo_actividad_id = 10
            ) t
            GROUP BY t.categoria
            ORDER BY t.categoria
            """;

        @SuppressWarnings("unchecked")
        List<Object[]> results2 = entityManager.createNativeQuery(sql)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("institucionId", institucionId)
                .getResultList();
        return results2;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenAntropometriaPesoEdadMenores2ConsultasRegularesPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        String sql = """
            SELECT t.categoria,
                   COUNT(*) AS total
            FROM (
                SELECT
                    CASE
                        WHEN z.valor_calculado > 3 THEN 'Exceso Moderado y Grave'
                        WHEN (z.valor_calculado IS NULL OR z.valor_calculado <= 3) AND p.valor_calculado > 97 THEN 'Exceso Leve'
                        WHEN p.valor_calculado > 90 AND p.valor_calculado <= 97 THEN 'Riesgo de Exceso'
                        WHEN p.valor_calculado > 10 AND p.valor_calculado <= 90 THEN 'Normal'
                        WHEN p.valor_calculado > 3 AND p.valor_calculado <= 10 THEN 'Riesgo de Déficit'
                        WHEN p.valor_calculado <= 3 AND (z.valor_calculado IS NULL OR z.valor_calculado > -3) THEN 'Déficit Leve'
                        WHEN z.valor_calculado <= -3 AND z.valor_calculado > -4 THEN 'Déficit Moderado'
                        WHEN z.valor_calculado <= -4 THEN 'Déficit Grave'
                        ELSE 'Sin Clasificación'
                    END AS categoria
                FROM evaluaciones_antropometricas z
                LEFT JOIN evaluaciones_antropometricas p ON p.consulta_id = z.consulta_id
                    AND p.tipo_indicador_id = 4
                    AND p.tipo_valor_calculado = 'PERCENTIL'
                    AND p.is_deleted = FALSE
                JOIN consultas c ON c.id = z.consulta_id
                JOIN pacientes pa ON pa.id = c.paciente_id
                JOIN usuarios_instituciones ui ON ui.id = c.usuario_institucion_id AND ui.is_enabled = TRUE
                WHERE z.tipo_indicador_id = 4
                  AND z.tipo_valor_calculado = 'Z_SCORE'
                  AND z.is_deleted = FALSE
                  AND c.is_deleted = FALSE
                  AND pa.is_deleted = FALSE
                  AND c.estado IN ('COMPLETADA','SIN_PREVIA_CITA')
                  AND c.fecha_hora_real::date BETWEEN :fechaInicio AND :fechaFin
                  AND ui.institucion_id = :institucionId
                  AND EXTRACT(YEAR FROM age(c.fecha_hora_real::date, pa.fecha_nacimiento))::int < 2
                  AND c.tipo_actividad_id = 1
            ) t
            GROUP BY t.categoria
            ORDER BY t.categoria
            """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("institucionId", institucionId)
                .getResultList();
        return results;
    }
}