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
}