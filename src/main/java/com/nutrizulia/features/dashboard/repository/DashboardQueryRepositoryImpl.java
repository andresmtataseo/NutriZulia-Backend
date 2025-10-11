package com.nutrizulia.features.dashboard.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DashboardQueryRepositoryImpl implements DashboardQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> consultasPorMes(LocalDate fechaInicio, LocalDate fechaFin, Integer municipioId, Integer institucionId) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT to_char(c.fecha_hora_real::date, 'YYYY-MM') AS periodo,
                   COUNT(*) AS total
            FROM consultas c
            JOIN usuarios_instituciones ui ON ui.id = c.usuario_institucion_id AND ui.is_enabled = TRUE
            JOIN instituciones i ON i.id = ui.institucion_id
            JOIN municipios_sanitarios ms ON ms.id = i.municipio_sanitario_id
            WHERE c.is_deleted = FALSE
              AND c.estado IN ('COMPLETADA','SIN_PREVIA_CITA')
              AND c.fecha_hora_real::date BETWEEN :fechaInicio AND :fechaFin
        """);
        if (municipioId != null) {
            sql.append(" AND ms.id = :municipioId");
        }
        if (institucionId != null) {
            sql.append(" AND i.id = :institucionId");
        }
        sql.append(" GROUP BY periodo ORDER BY periodo");

        @SuppressWarnings("unchecked")
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        if (municipioId != null) {
            query.setParameter("municipioId", municipioId);
        }
        if (institucionId != null) {
            query.setParameter("institucionId", institucionId);
        }
        List<Object[]> rows = query.getResultList();
        return rows;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> institucionesActivasPorMunicipio(Integer municipioId) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT ms.nombre AS municipio, COUNT(DISTINCT i.id) AS total
            FROM instituciones i
            JOIN municipios_sanitarios ms ON ms.id = i.municipio_sanitario_id
            JOIN usuarios_instituciones ui ON ui.institucion_id = i.id AND ui.is_enabled = TRUE
            WHERE 1=1
        """);
        if (municipioId != null) {
            sql.append(" AND ms.id = :municipioId");
        }
        sql.append(" GROUP BY ms.nombre ORDER BY ms.nombre");
        try {
            jakarta.persistence.Query query = entityManager.createNativeQuery(sql.toString());
            if (municipioId != null) {
                query.setParameter("municipioId", municipioId);
            }
            @SuppressWarnings("unchecked")
            List<Object[]> rows = query.getResultList();
            return rows;
        } catch (Exception ex) {
            log.error("Error ejecutando institucionesActivasPorMunicipio. municipioId={}, sql={}", municipioId, sql, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> usuariosActivosPorInstitucion(Integer municipioId) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT i.nombre AS institucion, COUNT(DISTINCT u.id) AS total
            FROM usuarios u
            JOIN usuarios_instituciones ui ON ui.usuario_id = u.id AND ui.is_enabled = TRUE
            JOIN instituciones i ON i.id = ui.institucion_id
            JOIN municipios_sanitarios ms ON ms.id = i.municipio_sanitario_id
            WHERE u.is_enabled = TRUE
        """);
        if (municipioId != null) {
            sql.append(" AND ms.id = :municipioId");
        }
        sql.append(" GROUP BY i.nombre ORDER BY i.nombre");
        String finalSql = sql.toString();
        try {
            jakarta.persistence.Query query = entityManager.createNativeQuery(finalSql);
            if (municipioId != null) {
                query.setParameter("municipioId", municipioId);
            }
            @SuppressWarnings("unchecked")
            List<Object[]> rows = query.getResultList();
            return rows;
        } catch (Exception ex) {
            log.error("Error ejecutando usuariosActivosPorInstitucion. municipioId={}, sql={}", municipioId, finalSql, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> distribucionGrupoEtario(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT bucket AS grupo, COUNT(*) AS total
            FROM (
                SELECT CASE
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int < 2 THEN 'LT2'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 2 AND 6 THEN 'A2_6'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 7 AND 12 THEN 'A7_12'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 13 AND 18 THEN 'A13_18'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 19 AND 59 THEN 'A19_59'
                    ELSE 'GTE60'
                END AS bucket
                FROM consultas c
                JOIN pacientes p ON p.id = c.paciente_id AND p.is_deleted = FALSE
                JOIN usuarios_instituciones ui ON ui.id = c.usuario_institucion_id AND ui.is_enabled = TRUE
                WHERE c.is_deleted = FALSE
                  AND c.estado IN ('COMPLETADA','SIN_PREVIA_CITA')
                  AND c.fecha_hora_real::date BETWEEN :fechaInicio AND :fechaFin
        """);
        if (institucionId != null) {
            sql.append(" AND ui.institucion_id = :institucionId");
        }
        sql.append(" ) t GROUP BY bucket ORDER BY bucket");
        @SuppressWarnings("unchecked")
        jakarta.persistence.Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        if (institucionId != null) {
            query.setParameter("institucionId", institucionId);
        }
        List<Object[]> rows = query.getResultList();
        return rows;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> estadoNutricionalPorGrupoEtario(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT bucket AS grupo, categoria, COUNT(*) AS total
            FROM (
                SELECT CASE
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int < 2 THEN 'LT2'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 2 AND 6 THEN 'A2_6'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 7 AND 12 THEN 'A7_12'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 13 AND 18 THEN 'A13_18'
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int BETWEEN 19 AND 59 THEN 'A19_59'
                    ELSE 'GTE60'
                END AS bucket,
                CASE
                    WHEN EXTRACT(YEAR FROM age(c.fecha_hora_real::date, p.fecha_nacimiento))::int < 19 THEN
                        CASE
                            WHEN COALESCE(imcedad.valor_calculado, -1) BETWEEN 97.7 AND 100 THEN 'Obesidad'
                            WHEN COALESCE(imcedad.valor_calculado, -1) BETWEEN 84.1 AND 97.6 THEN 'Sobrepeso'
                            WHEN COALESCE(imcedad.valor_calculado, -1) BETWEEN 2.3 AND 84.0 THEN 'Normal'
                            WHEN COALESCE(imcedad.valor_calculado, -1) BETWEEN 0.1 AND 2.2 THEN 'Delgadez'
                            WHEN COALESCE(imcedad.valor_calculado, -1) BETWEEN 0.0 AND 0.09 THEN 'Delgadez Severa'
                            ELSE 'Sin Clasificación'
                        END
                    ELSE
                        CASE
                            WHEN COALESCE(imc.valor_calculado, -1) BETWEEN 40.000000 AND 99.999999 THEN 'Obesidad Mórbida'
                            WHEN COALESCE(imc.valor_calculado, -1) BETWEEN 30.000000 AND 39.999999 THEN 'Obesidad'
                            WHEN COALESCE(imc.valor_calculado, -1) BETWEEN 25.000000 AND 29.999999 THEN 'Sobrepeso'
                            WHEN COALESCE(imc.valor_calculado, -1) BETWEEN 18.500000 AND 24.999999 THEN 'Normal'
                            WHEN COALESCE(imc.valor_calculado, -1) BETWEEN 17.000000 AND 18.499999 THEN 'Delgadez Leve'
                            WHEN COALESCE(imc.valor_calculado, -1) BETWEEN 16.000000 AND 16.999999 THEN 'Delgadez Moderada'
                            WHEN COALESCE(imc.valor_calculado, -1) BETWEEN 0.000000 AND 15.999999 THEN 'Delgadez Intensa'
                            ELSE 'Sin Clasificación'
                        END
                 END AS categoria
                 FROM consultas c
                 JOIN pacientes p ON p.id = c.paciente_id AND p.is_deleted = FALSE
                 JOIN usuarios_instituciones ui ON ui.id = c.usuario_institucion_id AND ui.is_enabled = TRUE
                                -- JOINs legacy removidos: bmi y ae
                                LEFT JOIN LATERAL (
                                    SELECT ea.valor_calculado
                                    FROM evaluaciones_antropometricas ea
                                    WHERE ea.consulta_id = c.id
                                      AND ea.tipo_valor_calculado = 'PERCENTIL'
                                      AND ea.tipo_indicador_id = 1 -- IMC/Edad
                                      AND ea.is_deleted = FALSE
                                    ORDER BY ea.id DESC
                                    LIMIT 1
                                ) imcedad ON TRUE
                                LEFT JOIN LATERAL (
                                    SELECT ea.valor_calculado
                                    FROM evaluaciones_antropometricas ea
                                    WHERE ea.consulta_id = c.id
                                      AND ea.tipo_valor_calculado = 'IMC'
                                      AND ea.tipo_indicador_id = 8 -- IMC
                                      AND ea.is_deleted = FALSE
                                    ORDER BY ea.id DESC
                                    LIMIT 1
                                ) imc ON TRUE
                 WHERE c.is_deleted = FALSE
                   AND c.estado IN ('COMPLETADA','SIN_PREVIA_CITA')
                   AND c.fecha_hora_real::date BETWEEN :fechaInicio AND :fechaFin
        """);
        if (institucionId != null) {
            sql.append(" AND ui.institucion_id = :institucionId");
        }
        sql.append(" ) t GROUP BY bucket, categoria ORDER BY bucket, categoria");
        jakarta.persistence.Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        if (institucionId != null) {
            query.setParameter("institucionId", institucionId);
        }
        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        return rows;
    }
}