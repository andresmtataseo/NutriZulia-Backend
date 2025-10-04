package com.nutrizulia.features.reports.service;

import com.nutrizulia.features.catalog.dto.InstitucionDto;
import com.nutrizulia.features.catalog.service.impl.InstitucionService;
import com.nutrizulia.features.reports.dto.ResumenConsultasEdadDto;
import com.nutrizulia.features.reports.repository.ReportsQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jxls.transform.Transformer;
import org.springframework.stereotype.Service;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteService {

    private final InstitucionService institucionService;
    private final ReportsQueryRepository reportsQueryRepository;

    public List<ResumenConsultasEdadDto> obtenerResumenConsultasPorEdadPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenConsultasPorEdadPorInstitucion(fechaInicio, fechaFin, institucionId);
        List<ResumenConsultasEdadDto> result = new ArrayList<>();
        for (Object[] row : raw) {
            String rango = (String) row[0];
            Integer totalPrimeras = toInteger(row[1]);
            Integer totalSucesivas = toInteger(row[2]);
            result.add(ResumenConsultasEdadDto.builder()
                    .rango(rango)
                    .totalPrimeras(totalPrimeras)
                    .totalSucesivas(totalSucesivas)
                    .build());
        }
        return result;
    }

    // Nuevo: obtener resumen de actividades por tipo para una institución y rango de fechas
    private List<ActividadTipoResumen> obtenerResumenActividadesPorTipoPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenActividadesPorTipoPorInstitucion(fechaInicio, fechaFin, institucionId);
        List<ActividadTipoResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            Integer tipoId = toInteger(row[0]);
            String tipoNombre = (String) row[1];
            Integer totalParticipantes = toInteger(row[2]);
            Integer totalVeces = toInteger(row[3]);
            result.add(new ActividadTipoResumen(tipoId, tipoNombre, totalParticipantes, totalVeces));
        }
        return result;
    }

    private Integer toInteger(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer i) return i;
        if (value instanceof Long l) return l.intValue();
        if (value instanceof Number n) return n.intValue();
        return Integer.parseInt(value.toString());
    }

    // Record para encapsular los datos de cada mes
    private record MesContext(List<Map<String, Object>> lista, Map<Integer, Map<String, Integer>> porInstitucion, Map<Integer, Map<String, Integer>> actividadesPorInstitucion) {}

    // Record para resumen de actividades por tipo
    private record ActividadTipoResumen(Integer tipoId, String tipoNombre, Integer totalParticipantes, Integer totalVeces) {}

    // Calcula los datos del contexto por mes, eliminando código repetitivo
    private MesContext calcularContextoMes(List<InstitucionDto> instituciones, LocalDate fechaInicioMes, LocalDate fechaFinMes) {
        List<Map<String, Object>> datosMes = new ArrayList<>();
        Map<Integer, Map<String, Integer>> mesPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> actividadesPorInstitucion = new HashMap<>();
        for (InstitucionDto ins : instituciones) {
            List<ResumenConsultasEdadDto> resumen = obtenerResumenConsultasPorEdadPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<ActividadTipoResumen> actividadesResumen = obtenerResumenActividadesPorTipoPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());

            Map<String, Object> fila = new HashMap<>();
            fila.put("institucion", ins);
            fila.put("resumen", resumen);
            fila.put("actividades", actividadesResumen);
            Map<String, Integer> plano = aplanarResumenPorRango(resumen);
            Map<String, Integer> actividadesPlanas = aplanarActividadesPorTipo(actividadesResumen);
            fila.put("actividadesPlanas", actividadesPlanas);
            datosMes.add(fila);

            // Resumen aplanado por rango para acceso directo desde la plantilla
            mesPorInstitucion.put(ins.getId(), plano);
            actividadesPorInstitucion.put(ins.getId(), actividadesPlanas);
        }
        return new MesContext(datosMes, mesPorInstitucion, actividadesPorInstitucion);
    }

    // Helper: Aplana la lista de ResumenConsultasEdadDto a un mapa por rango
    private Map<String, Integer> aplanarResumenPorRango(List<ResumenConsultasEdadDto> resumen) {
        Map<String, Integer> flat = new HashMap<>();
        // Inicializar todas las claves con 0 para evitar nulls en la plantilla
        flat.put("lt2Primeras", 0);        flat.put("lt2Sucesivas", 0);
        flat.put("a2_6Primeras", 0);       flat.put("a2_6Sucesivas", 0);
        flat.put("a7_12Primeras", 0);      flat.put("a7_12Sucesivas", 0);
        flat.put("a12_19Primeras", 0);     flat.put("a12_19Sucesivas", 0);
        flat.put("a19_60Primeras", 0);     flat.put("a19_60Sucesivas", 0);
        flat.put("gte60Primeras", 0);      flat.put("gte60Sucesivas", 0);
        flat.put("prenatalPrimeras", 0);   flat.put("prenatalSucesivas", 0);

        for (ResumenConsultasEdadDto r : resumen) {
            String rango = r.getRango();
            Integer primeras = r.getTotalPrimeras() != null ? r.getTotalPrimeras() : 0;
            Integer sucesivas = r.getTotalSucesivas() != null ? r.getTotalSucesivas() : 0;
            if ("LT2".equals(rango)) {
                flat.put("lt2Primeras", primeras);
                flat.put("lt2Sucesivas", sucesivas);
            } else if ("A2_6".equals(rango)) {
                flat.put("a2_6Primeras", primeras);
                flat.put("a2_6Sucesivas", sucesivas);
            } else if ("A7_12".equals(rango)) {
                flat.put("a7_12Primeras", primeras);
                flat.put("a7_12Sucesivas", sucesivas);
            } else if ("A12_19".equals(rango)) {
                flat.put("a12_19Primeras", primeras);
                flat.put("a12_19Sucesivas", sucesivas);
            } else if ("A19_60".equals(rango)) {
                flat.put("a19_60Primeras", primeras);
                flat.put("a19_60Sucesivas", sucesivas);
            } else if ("GTE60".equals(rango)) {
                flat.put("gte60Primeras", primeras);
                flat.put("gte60Sucesivas", sucesivas);
            } else if ("PRENATAL".equals(rango)) {
                flat.put("prenatalPrimeras", primeras);
                flat.put("prenatalSucesivas", sucesivas);
            }
        }
        return flat;
    }

    // Helper: Aplana la lista de actividades por tipo a un mapa con claves por id de tipo
    private Map<String, Integer> aplanarActividadesPorTipo(List<ActividadTipoResumen> actividadesResumen) {
        Map<String, Integer> flat = new HashMap<>();
        // Inicializar ids 2..13 con 0
        for (int id = 2; id <= 13; id++) {
            flat.put("act" + id + "Participantes", 0);
            flat.put("act" + id + "Veces", 0);
        }
        for (ActividadTipoResumen a : actividadesResumen) {
            Integer tipoId = a.tipoId();
            if (tipoId != null && tipoId >= 2 && tipoId <= 13) {
                flat.put("act" + tipoId + "Participantes", a.totalParticipantes() != null ? a.totalParticipantes() : 0);
                flat.put("act" + tipoId + "Veces", a.totalVeces() != null ? a.totalVeces() : 0);
            }
        }
        return flat;
    }

    public void generarReporteTrimestralPorMunicipio(Integer municipioSanitarioId, Integer anio, OutputStream os) throws Exception {
        
        if (municipioSanitarioId == null || municipioSanitarioId <= 0) {
            log.warn("ID de municipio sanitario inválido: {}", municipioSanitarioId);
            throw new IllegalArgumentException("El ID del municipio sanitario debe ser un número positivo");
        }
        if (anio == null || anio < 1900 || anio > 2100) {
            log.warn("Año del reporte inválido: {}", anio);
            throw new IllegalArgumentException("El año del reporte debe ser un número válido entre 1900 y 2100");
        }

        List<InstitucionDto> instituciones = institucionService.getInstitucionesByMunicipioSanitario(municipioSanitarioId);
        
        if (instituciones.isEmpty()) {
            log.warn("No se encontraron instituciones para el municipio sanitario ID: {}", municipioSanitarioId);
            throw new RuntimeException("No se encontraron instituciones para el municipio sanitario especificado");
        }

        try (InputStream is = getClass().getResourceAsStream("/templates/resumen_trimestral.xlsx")) {
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar la plantilla resumen_trimestral.xlsx");
            }

            Context context = new Context();
            // Lista de instituciones disponible para hojas que lo requieran
            context.putVar("instituciones", instituciones);

            // Construir datos por mes (mes1..mes12) por institución, para el año solicitado
            final int year = anio;
            for (int m = 1; m <= 12; m++) {
                YearMonth ym = YearMonth.of(year, m);
                LocalDate fechaInicioMes = ym.atDay(1);
                LocalDate fechaFinMes = ym.atEndOfMonth();

                MesContext mc = calcularContextoMes(instituciones, fechaInicioMes, fechaFinMes);
                context.putVar("mes" + m, mc.lista());
                context.putVar("mes" + m + "PorInstitucion", mc.porInstitucion());
                // Nuevo: actividades por tipo (participantes y veces) aplanadas por institución
                context.putVar("mes" + m + "ActividadesPorInstitucion", mc.actividadesPorInstitucion());
            }
            
            // Debug: Verificar el contexto JXLS
            log.info("Contexto JXLS creado con {} instituciones y datos del año {}", instituciones.size(), year);
            
            // Configurar JXLS para mejor procesamiento
            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            jxlsHelper.setHideTemplateSheet(true);
            jxlsHelper.setDeleteTemplateSheet(true);
            Transformer transformer  = jxlsHelper.createTransformer(is, os);
            jxlsHelper.processTemplate(context, transformer);
            log.info("Reporte generado exitosamente en memoria");
        }
    }
}