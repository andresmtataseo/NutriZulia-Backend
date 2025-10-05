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
    private record MesContext(List<Map<String, Object>> lista, Map<Integer, Map<String, Integer>> porInstitucion, Map<Integer, Map<String, Integer>> actividadesPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaRegularesPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos2a9PorInstitucion, Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos2a9RegularesPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos10a18PorInstitucion) {}

    // Record para resumen de actividades por tipo
    private record ActividadTipoResumen(Integer tipoId, String tipoNombre, Integer totalParticipantes, Integer totalVeces) {}

    // Nuevo: Record para resumen antropométrico Peso/Edad menores de 2 años
    private record AntropometriaResumen(String categoria, Integer total) {}

    // Calcula los datos del contexto por mes, eliminando código repetitivo
    private MesContext calcularContextoMes(List<InstitucionDto> instituciones, LocalDate fechaInicioMes, LocalDate fechaFinMes) {
        List<Map<String, Object>> datosMes = new ArrayList<>();
        Map<Integer, Map<String, Integer>> mesPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> actividadesPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> antropometriaPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> antropometriaRegularesPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos2a9PorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos2a9RegularesPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos10a18PorInstitucion = new HashMap<>();
        for (InstitucionDto ins : instituciones) {
            List<ResumenConsultasEdadDto> resumen = obtenerResumenConsultasPorEdadPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<ActividadTipoResumen> actividadesResumen = obtenerResumenActividadesPorTipoPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaResumen = obtenerResumenAntropometriaPesoEdadMenores2PorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaResumenRegulares = obtenerResumenAntropometriaPesoEdadMenores2ConsultasRegularesPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaCombinadaNinos2a9Resumen = obtenerResumenAntropometriaCombinadaNinos2a9PorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaCombinadaNinos2a9ResumenRegulares = obtenerResumenAntropometriaCombinadaNinos2a9ConsultasRegularesPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaCombinadaNinos10a18Resumen = obtenerResumenAntropometriaCombinadaNinos10a18PorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());

            Map<String, Object> fila = new HashMap<>();
            fila.put("institucion", ins);
            fila.put("resumen", resumen);
            fila.put("actividades", actividadesResumen);
            fila.put("antropometria", antropometriaResumen);
            fila.put("antropometriaRegulares", antropometriaResumenRegulares);
            fila.put("antropometriaCombinadaNinos2a9", antropometriaCombinadaNinos2a9Resumen);
            fila.put("antropometriaCombinadaNinos2a9Regulares", antropometriaCombinadaNinos2a9ResumenRegulares);
            fila.put("antropometriaCombinadaNinos10a18", antropometriaCombinadaNinos10a18Resumen);
            Map<String, Integer> plano = aplanarResumenPorRango(resumen);
            Map<String, Integer> actividadesPlanas = aplanarActividadesPorTipo(actividadesResumen);
            Map<String, Integer> antropometriaPlana = aplanarAntropometriaMenores2(antropometriaResumen);
            Map<String, Integer> antropometriaRegularesPlana = aplanarAntropometriaMenores2(antropometriaResumenRegulares);
            Map<String, Integer> antropometriaCombinadaNinos2a9Plana = aplanarAntropometriaCombinadaNinos2a9(antropometriaCombinadaNinos2a9Resumen);
            Map<String, Integer> antropometriaCombinadaNinos2a9RegularesPlana = aplanarAntropometriaCombinadaNinos2a9(antropometriaCombinadaNinos2a9ResumenRegulares);
            Map<String, Integer> antropometriaCombinadaNinos10a18Plana = aplanarAntropometriaCombinadaNinos10a18(antropometriaCombinadaNinos10a18Resumen);
            fila.put("actividadesPlanas", actividadesPlanas);
            fila.put("antropometriaPlana", antropometriaPlana);
            fila.put("antropometriaRegularesPlana", antropometriaRegularesPlana);
            fila.put("antropometriaCombinadaNinos2a9Plana", antropometriaCombinadaNinos2a9Plana);
            fila.put("antropometriaCombinadaNinos2a9RegularesPlana", antropometriaCombinadaNinos2a9RegularesPlana);
            fila.put("antropometriaCombinadaNinos10a18Plana", antropometriaCombinadaNinos10a18Plana);
            datosMes.add(fila);

            // Resumen aplanado por rango para acceso directo desde la plantilla
            mesPorInstitucion.put(ins.getId(), plano);
            actividadesPorInstitucion.put(ins.getId(), actividadesPlanas);
            antropometriaPorInstitucion.put(ins.getId(), antropometriaPlana);
            antropometriaRegularesPorInstitucion.put(ins.getId(), antropometriaRegularesPlana);
            antropometriaCombinadaNinos2a9PorInstitucion.put(ins.getId(), antropometriaCombinadaNinos2a9Plana);
            antropometriaCombinadaNinos2a9RegularesPorInstitucion.put(ins.getId(), antropometriaCombinadaNinos2a9RegularesPlana);
            antropometriaCombinadaNinos10a18PorInstitucion.put(ins.getId(), antropometriaCombinadaNinos10a18Plana);
        }
        return new MesContext(datosMes, mesPorInstitucion, actividadesPorInstitucion, antropometriaPorInstitucion, antropometriaRegularesPorInstitucion, antropometriaCombinadaNinos2a9PorInstitucion, antropometriaCombinadaNinos2a9RegularesPorInstitucion, antropometriaCombinadaNinos10a18PorInstitucion);
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

    // Nuevo: obtener resumen antropométrico Peso/Edad en menores de 2 años por institución y rango de fechas
    private List<AntropometriaResumen> obtenerResumenAntropometriaPesoEdadMenores2PorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenAntropometriaPesoEdadMenores2PorInstitucion(fechaInicio, fechaFin, institucionId);
        List<AntropometriaResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            String categoria = (String) row[0];
            Integer total = toInteger(row[1]);
            result.add(new AntropometriaResumen(categoria, total));
        }
        return result;
    }

    private List<AntropometriaResumen> obtenerResumenAntropometriaPesoEdadMenores2ConsultasRegularesPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenAntropometriaPesoEdadMenores2ConsultasRegularesPorInstitucion(fechaInicio, fechaFin, institucionId);
        List<AntropometriaResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            String categoria = (String) row[0];
            Integer total = toInteger(row[1]);
            result.add(new AntropometriaResumen(categoria, total));
        }
        return result;
    }

    private List<AntropometriaResumen> obtenerResumenAntropometriaCombinadaNinos2a9PorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenAntropometriaCombinadaNinos2a9PorInstitucion(fechaInicio, fechaFin, institucionId);
        List<AntropometriaResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            String categoria = (String) row[0];
            Integer total = toInteger(row[1]);
            result.add(new AntropometriaResumen(categoria, total));
        }
        return result;
    }

    private List<AntropometriaResumen> obtenerResumenAntropometriaCombinadaNinos2a9ConsultasRegularesPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenAntropometriaCombinadaNinos2a9ConsultasRegularesPorInstitucion(fechaInicio, fechaFin, institucionId);
        List<AntropometriaResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            String categoria = (String) row[0];
            Integer total = toInteger(row[1]);
            result.add(new AntropometriaResumen(categoria, total));
        }
        return result;
    }

    private List<AntropometriaResumen> obtenerResumenAntropometriaCombinadaNinos10a18PorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenAntropometriaCombinadaNinos10a18PorInstitucion(fechaInicio, fechaFin, institucionId);
        List<AntropometriaResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            String categoria = (String) row[0];
            Integer total = toInteger(row[1]);
            result.add(new AntropometriaResumen(categoria, total));
        }
        return result;
    }

    // Helper: Aplana el resumen antropométrico de Peso/Edad menores de 2 años a un mapa con claves fijas
    private Map<String, Integer> aplanarAntropometriaMenores2(List<AntropometriaResumen> antropometriaResumen) {
        Map<String, Integer> flat = new HashMap<>();
        // Inicializar claves esperadas para evitar nulls en la plantilla
        flat.put("pesoEdadMenores2ExcesoModeradoGrave", 0);
        flat.put("pesoEdadMenores2ExcesoLeve", 0);
        flat.put("pesoEdadMenores2RiesgoExceso", 0);
        flat.put("pesoEdadMenores2Normal", 0);
        flat.put("pesoEdadMenores2RiesgoDeficit", 0);
        flat.put("pesoEdadMenores2DeficitLeve", 0);
        flat.put("pesoEdadMenores2DeficitModerado", 0);
        flat.put("pesoEdadMenores2DeficitGrave", 0);
        flat.put("pesoEdadMenores2SinClasificacion", 0);

        for (AntropometriaResumen a : antropometriaResumen) {
            String cat = a.categoria();
            Integer total = a.total() != null ? a.total() : 0;
            if ("Exceso Moderado y Grave".equals(cat)) {
                flat.put("pesoEdadMenores2ExcesoModeradoGrave", total);
            } else if ("Exceso Leve".equals(cat)) {
                flat.put("pesoEdadMenores2ExcesoLeve", total);
            } else if ("Riesgo de Exceso".equals(cat)) {
                flat.put("pesoEdadMenores2RiesgoExceso", total);
            } else if ("Normal".equals(cat)) {
                flat.put("pesoEdadMenores2Normal", total);
            } else if ("Riesgo de Déficit".equals(cat)) {
                flat.put("pesoEdadMenores2RiesgoDeficit", total);
            } else if ("Déficit Leve".equals(cat)) {
                flat.put("pesoEdadMenores2DeficitLeve", total);
            } else if ("Déficit Moderado".equals(cat)) {
                flat.put("pesoEdadMenores2DeficitModerado", total);
            } else if ("Déficit Grave".equals(cat)) {
                flat.put("pesoEdadMenores2DeficitGrave", total);
            } else if ("Sin Clasificación".equals(cat)) {
                flat.put("pesoEdadMenores2SinClasificacion", total);
            }
        }
        return flat;
    }

    // Nuevo: Aplana el resumen antropométrico combinado (P/T y T/E) para niños de 2 a 9 años 11 meses
    private Map<String, Integer> aplanarAntropometriaCombinadaNinos2a9(List<AntropometriaResumen> resumen) {
        Map<String, Integer> flat = new HashMap<>();
        // Inicializar claves para evitar nulls en la plantilla
        flat.put("antropometriaCombinadaNinos2a9Sobrepeso", 0);
        flat.put("antropometriaCombinadaNinos2a9Normal", 0);
        flat.put("antropometriaCombinadaNinos2a9DeficitAgudo", 0);
        flat.put("antropometriaCombinadaNinos2a9DeficitCronicoCompensado", 0);
        flat.put("antropometriaCombinadaNinos2a9DeficitCronicoDescompensado", 0);
        flat.put("antropometriaCombinadaNinos2a9SinClasificacion", 0);

        for (AntropometriaResumen a : resumen) {
            String categoria = a.categoria();
            Integer total = a.total() != null ? a.total() : 0;
            switch (categoria) {
                case "Sobrepeso" -> flat.put("antropometriaCombinadaNinos2a9Sobrepeso", total);
                case "Normal" -> flat.put("antropometriaCombinadaNinos2a9Normal", total);
                case "Déficit Agudo" -> flat.put("antropometriaCombinadaNinos2a9DeficitAgudo", total);
                case "Déficit Crónico Compensado" -> flat.put("antropometriaCombinadaNinos2a9DeficitCronicoCompensado", total);
                case "Déficit Crónico Descompensado" -> flat.put("antropometriaCombinadaNinos2a9DeficitCronicoDescompensado", total);
                case "Sin Clasificación" -> flat.put("antropometriaCombinadaNinos2a9SinClasificacion", total);
                default -> {
                    int existente = flat.getOrDefault("antropometriaCombinadaNinos2a9SinClasificacion", 0);
                    flat.put("antropometriaCombinadaNinos2a9SinClasificacion", existente + total);
                }
            }
        }
        return flat;
    }

    // Nuevo: Aplana el resumen antropométrico combinado (IMC/E y T/E) para niños de 10 a 18 años 11 meses
    private Map<String, Integer> aplanarAntropometriaCombinadaNinos10a18(List<AntropometriaResumen> resumen) {
        Map<String, Integer> flat = new HashMap<>();
        // Inicializar claves para evitar nulls en la plantilla
        flat.put("antropometriaCombinadaNinos10a18Sobrepeso", 0);
        flat.put("antropometriaCombinadaNinos10a18Normal", 0);
        flat.put("antropometriaCombinadaNinos10a18DeficitAgudo", 0);
        flat.put("antropometriaCombinadaNinos10a18DeficitCronicoCompensado", 0);
        flat.put("antropometriaCombinadaNinos10a18DeficitCronicoDescompensado", 0);
        flat.put("antropometriaCombinadaNinos10a18SinClasificacion", 0);

        for (AntropometriaResumen a : resumen) {
            String categoria = a.categoria();
            Integer total = a.total() != null ? a.total() : 0;
            switch (categoria) {
                case "Sobrepeso" -> flat.put("antropometriaCombinadaNinos10a18Sobrepeso", total);
                case "Normal" -> flat.put("antropometriaCombinadaNinos10a18Normal", total);
                case "Déficit Agudo" -> flat.put("antropometriaCombinadaNinos10a18DeficitAgudo", total);
                case "Déficit Crónico Compensado" -> flat.put("antropometriaCombinadaNinos10a18DeficitCronicoCompensado", total);
                case "Déficit Crónico Descompensado" -> flat.put("antropometriaCombinadaNinos10a18DeficitCronicoDescompensado", total);
                case "Sin Clasificación" -> flat.put("antropometriaCombinadaNinos10a18SinClasificacion", total);
                default -> {
                    int existente = flat.getOrDefault("antropometriaCombinadaNinos10a18SinClasificacion", 0);
                    flat.put("antropometriaCombinadaNinos10a18SinClasificacion", existente + total);
                }
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
                // Nuevo: antropometría Peso/Edad menores de 2 años aplanada por institución
                context.putVar("mes" + m + "AntropometriaPorInstitucion", mc.antropometriaPorInstitucion());
                // Nuevo: antropometría Peso/Edad menores de 2 años para consultas regulares (tipo_actividad_id=1) aplanada por institución
                context.putVar("mes" + m + "AntropometriaRegularesPorInstitucion", mc.antropometriaRegularesPorInstitucion());
                // Nuevo: antropometría combinada (P/T y T/E) para niños 2-9 años 11 meses (tipo_actividad_id=10) aplanada por institución
                context.putVar("mes" + m + "AntropometriaCombinadaNinos2a9PorInstitucion", mc.antropometriaCombinadaNinos2a9PorInstitucion());
                // Nuevo: sección paralela de antropometría combinada para consultas regulares (tipo_actividad_id=1) aplanada por institución
                context.putVar("mes" + m + "AntropometriaCombinadaNinos2a9RegularesPorInstitucion", mc.antropometriaCombinadaNinos2a9RegularesPorInstitucion());
                // Nuevo: antropometría combinada (IMC/E y T/E) para niños 10-18 años 11 meses (tipo_actividad_id=10) aplanada por institución
                context.putVar("mes" + m + "AntropometriaCombinadaNinos10a18PorInstitucion", mc.antropometriaCombinadaNinos10a18PorInstitucion());
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