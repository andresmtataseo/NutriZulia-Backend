package com.nutrizulia.features.dashboard.service;

import com.nutrizulia.features.dashboard.dto.ChartResponseDto;
import com.nutrizulia.features.dashboard.dto.ChartSeriesDto;
import com.nutrizulia.features.dashboard.repository.DashboardQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardQueryRepository queryRepository;

    public DashboardServiceImpl(DashboardQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public ChartResponseDto getConsultasPorMes(YearMonth inicio, YearMonth fin, Integer municipioId, Integer institucionId) {
        LocalDate fInicio = inicio.atDay(1);
        LocalDate fFin = fin.atEndOfMonth();
        List<Object[]> rows = queryRepository.consultasPorMes(fInicio, fFin, municipioId, institucionId);
        List<String> labels = buildLabels(inicio, fin);
        Map<String, Integer> byPeriod = new LinkedHashMap<>();
        for (Object[] r : rows) {
            byPeriod.put((String) r[0], ((Number) r[1]).intValue());
        }
        double[] data = new double[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            String ym = labels.get(i);
            data[i] = byPeriod.getOrDefault(ym, 0);
        }
        ChartSeriesDto serie = new ChartSeriesDto("Consultas", data, "#2a9d8f");
        String title = String.format("Consultas por mes: %s - %s", inicio, fin);
        return new ChartResponseDto(title, labels, List.of(serie));
    }

    @Override
    public ChartResponseDto getInstitucionesActivasPorMunicipio(Integer municipioId) {
        List<Object[]> rows = queryRepository.institucionesActivasPorMunicipio(municipioId);
        List<String> labels = new ArrayList<>();
        double[] data = new double[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            Object[] r = rows.get(i);
            labels.add((String) r[0]);
            data[i] = ((Number) r[1]).doubleValue();
        }
        ChartSeriesDto serie = new ChartSeriesDto("Instituciones", data, "#e76f51");
        String title = "Instituciones activas por municipio (actual)";
        return new ChartResponseDto(title, labels, List.of(serie));
    }

    @Override
    public ChartResponseDto getUsuariosActivosPorInstitucion(Integer municipioId) {
        log.debug("getUsuariosActivosPorInstitucion municipioId={}", municipioId);
        List<Object[]> rows = queryRepository.usuariosActivosPorInstitucion(municipioId);
        log.debug("Filas recuperadas: {}", rows.size());
        List<String> labels = new ArrayList<>();
        double[] data = new double[rows.size()];
        try {
            for (int i = 0; i < rows.size(); i++) {
                Object[] r = rows.get(i);
                if (r == null || r.length < 2) {
                    log.warn("Fila inválida en posición {}: {}", i, r);
                    continue;
                }
                Object col0 = r[0];
                Object col1 = r[1];
                if (col0 != null) {
                    log.trace("Row {} col0 type={} value={}", i, col0.getClass().getName(), col0);
                }
                if (col1 != null) {
                    log.trace("Row {} col1 type={} value={}", i, col1.getClass().getName(), col1);
                }
                labels.add((String) col0);
                data[i] = ((Number) col1).doubleValue();
            }
        } catch (Exception ex) {
            log.error("Error mapeando resultados de usuariosActivosPorInstitucion municipioId={}", municipioId, ex);
            throw ex;
        }
        ChartSeriesDto serie = new ChartSeriesDto("Usuarios", data, "#264653");
        String title = "Usuarios activos por institución (actual)";
        return new ChartResponseDto(title, labels, List.of(serie));
    }

    @Override
    public ChartResponseDto getDistribucionGrupoEtario(YearMonth inicio, YearMonth fin, Integer institucionId) {
        LocalDate fInicio = inicio.atDay(1);
        LocalDate fFin = fin.atEndOfMonth();
        List<Object[]> rows = queryRepository.distribucionGrupoEtario(fInicio, fFin, institucionId);
        List<String> labels = new ArrayList<>();
        double[] data = new double[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            Object[] r = rows.get(i);
            labels.add((String) r[0]);
            data[i] = ((Number) r[1]).doubleValue();
        }
        ChartSeriesDto serie = new ChartSeriesDto("Pacientes", data, "#f4a261");
        String title = String.format("Distribución por grupo etario: %s - %s", inicio, fin);
        return new ChartResponseDto(title, labels, List.of(serie));
    }

    @Override
    public ChartResponseDto getEstadoNutricionalPorGrupoEtario(YearMonth inicio, YearMonth fin, Integer institucionId) {
        LocalDate fInicio = inicio.atDay(1);
        LocalDate fFin = fin.atEndOfMonth();
        List<Object[]> rows = queryRepository.estadoNutricionalPorGrupoEtario(fInicio, fFin, institucionId);

        Map<String, Map<String, Integer>> byGroupCategory = new LinkedHashMap<>();
        for (Object[] r : rows) {
            String grupo = (String) r[0];
            String categoria = (String) r[1];
            Integer total = ((Number) r[2]).intValue();
            byGroupCategory.computeIfAbsent(grupo, g -> new LinkedHashMap<>()).put(categoria, total);
        }

        // Reordenar labels según el orden deseado de grupos etarios
        List<String> orderedBuckets = List.of("LT2", "A2_6", "A7_12", "A13_18", "A19_59", "GTE60");
        List<String> labels = new ArrayList<>();
        for (String b : orderedBuckets) {
            if (byGroupCategory.containsKey(b)) {
                labels.add(b);
            }
        }
        // Categorías de diagnóstico antropométrico unificadas para infantes/adolescentes (IMC/Edad Percentil)
        // y adultos (IMC). El gráfico mostrará únicamente aquellas presentes en cada grupo.
        List<String> categorias = List.of(
                "Obesidad Mórbida",
                "Obesidad",
                "Sobrepeso",
                "Normal",
                "Delgadez Leve",
                "Delgadez Moderada",
                "Delgadez Intensa",
                "Delgadez",
                "Delgadez Severa",
                "Sin Clasificación"
        );
        Map<String, String> colores = new LinkedHashMap<>();
        colores.put("Obesidad Mórbida", "#8B0000");
        colores.put("Obesidad", "#c0392b");
        colores.put("Sobrepeso", "#e76f51");
        colores.put("Normal", "#2a9d8f");
        colores.put("Delgadez Leve", "#f4a261");
        colores.put("Delgadez Moderada", "#e9c46a");
        colores.put("Delgadez Intensa", "#d3862a");
        colores.put("Delgadez", "#a8dadc");
        colores.put("Delgadez Severa", "#457b9d");
        colores.put("Sin Clasificación", "#6c757d");

        List<ChartSeriesDto> series = new ArrayList<>();
        for (String cat : categorias) {
            double[] data = new double[labels.size()];
            for (int i = 0; i < labels.size(); i++) {
                String grupo = labels.get(i);
                int valor = byGroupCategory.getOrDefault(grupo, Map.of()).getOrDefault(cat, 0);
                data[i] = valor;
            }
            // Omitir categorías cuyo total sea 0 en todos los grupos para simplificar el gráfico
            double sum = 0;
            for (double v : data) { sum += v; }
            if (sum > 0) {
                series.add(new ChartSeriesDto(cat, data, colores.getOrDefault(cat, "#264653")));
            }
        }

        String title = String.format("Estado nutricional por grupo etario: %s - %s", inicio, fin);
        return new ChartResponseDto(title, labels, series);
    }

    private List<String> buildLabels(YearMonth inicio, YearMonth fin) {
        List<String> labels = new ArrayList<>();
        YearMonth current = inicio;
        while (!current.isAfter(fin)) {
            labels.add(current.toString());
            current = current.plusMonths(1);
        }
        return labels;
    }
}