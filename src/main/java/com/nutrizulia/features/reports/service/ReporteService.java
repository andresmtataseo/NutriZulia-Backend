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
import com.nutrizulia.features.catalog.repository.MunicipioSanitarioRepository;
import com.nutrizulia.features.reports.dto.InstitucionDataFreshnessDto;
import com.nutrizulia.features.reports.dto.UsuarioDataFreshnessDto;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteService {

    private final InstitucionService institucionService;
    private final ReportsQueryRepository reportsQueryRepository;
    private final MunicipioSanitarioRepository municipioSanitarioRepository;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;

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
    private record MesContext(List<Map<String, Object>> lista, Map<Integer, Map<String, Integer>> porInstitucion, Map<Integer, Map<String, Integer>> actividadesPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaRegularesPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos2a9PorInstitucion, Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos2a9RegularesPorInstitucion, Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos10a18PorInstitucion, Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos10a18RegularesPorInstitucion, Map<Integer, Map<String, Integer>> imcAdultosPorInstitucion, Map<Integer, Map<String, Integer>> riesgosBiologicosPorInstitucion) {}

    // Record para resumen de actividades por tipo
    private record ActividadTipoResumen(Integer tipoId, String tipoNombre, Integer totalParticipantes, Integer totalVeces) {}

    // Nuevo: Record para resumen antropométrico Peso/Edad menores de 2 años
    private record AntropometriaResumen(String categoria, Integer total) {}
    
    // Nuevo: Record para resumen IMC adultos por categoría y grupo de edad
    private record AntropometriaImcAdultosResumen(String categoria, String grupoEdad, Integer total) {}

    // Nuevo: Record para resumen de riesgos biológicos por nombre
    private record RiesgoBiologicoResumen(String nombre, Integer total) {}

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
        Map<Integer, Map<String, Integer>> antropometriaCombinadaNinos10a18RegularesPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> imcAdultosPorInstitucion = new HashMap<>();
        Map<Integer, Map<String, Integer>> riesgosBiologicosPorInstitucion = new HashMap<>();
        for (InstitucionDto ins : instituciones) {
            List<ResumenConsultasEdadDto> resumen = obtenerResumenConsultasPorEdadPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<ActividadTipoResumen> actividadesResumen = obtenerResumenActividadesPorTipoPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaResumen = obtenerResumenAntropometriaPesoEdadMenores2PorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaResumenRegulares = obtenerResumenAntropometriaPesoEdadMenores2ConsultasRegularesPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaCombinadaNinos2a9Resumen = obtenerResumenAntropometriaCombinadaNinos2a9PorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaCombinadaNinos2a9ResumenRegulares = obtenerResumenAntropometriaCombinadaNinos2a9ConsultasRegularesPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaCombinadaNinos10a18Resumen = obtenerResumenAntropometriaCombinadaNinos10a18PorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaResumen> antropometriaCombinadaNinos10a18ResumenRegulares = obtenerResumenAntropometriaCombinadaNinos10a18ConsultasRegularesPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<AntropometriaImcAdultosResumen> imcAdultosResumen = obtenerResumenImcAdultosPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());
            List<RiesgoBiologicoResumen> riesgosBiologicosResumen = obtenerResumenRiesgoBiologicoPorInstitucion(fechaInicioMes, fechaFinMes, ins.getId());

            Map<String, Object> fila = new HashMap<>();
            fila.put("institucion", ins);
            fila.put("resumen", resumen);
            fila.put("actividades", actividadesResumen);
            fila.put("antropometria", antropometriaResumen);
            fila.put("antropometriaRegulares", antropometriaResumenRegulares);
            fila.put("antropometriaCombinadaNinos2a9", antropometriaCombinadaNinos2a9Resumen);
            fila.put("antropometriaCombinadaNinos2a9Regulares", antropometriaCombinadaNinos2a9ResumenRegulares);
            fila.put("antropometriaCombinadaNinos10a18", antropometriaCombinadaNinos10a18Resumen);
            fila.put("antropometriaCombinadaNinos10a18Regulares", antropometriaCombinadaNinos10a18ResumenRegulares);
            fila.put("imcAdultos", imcAdultosResumen);
            fila.put("riesgosBiologicos", riesgosBiologicosResumen);
            Map<String, Integer> plano = aplanarResumenPorRango(resumen);
            Map<String, Integer> actividadesPlanas = aplanarActividadesPorTipo(actividadesResumen);
            Map<String, Integer> antropometriaPlana = aplanarAntropometriaMenores2(antropometriaResumen);
            Map<String, Integer> antropometriaRegularesPlana = aplanarAntropometriaMenores2(antropometriaResumenRegulares);
            Map<String, Integer> antropometriaCombinadaNinos2a9Plana = aplanarAntropometriaCombinadaNinos2a9(antropometriaCombinadaNinos2a9Resumen);
            Map<String, Integer> antropometriaCombinadaNinos2a9RegularesPlana = aplanarAntropometriaCombinadaNinos2a9(antropometriaCombinadaNinos2a9ResumenRegulares);
            Map<String, Integer> antropometriaCombinadaNinos10a18Plana = aplanarAntropometriaCombinadaNinos10a18(antropometriaCombinadaNinos10a18Resumen);
            Map<String, Integer> antropometriaCombinadaNinos10a18RegularesPlana = aplanarAntropometriaCombinadaNinos10a18(antropometriaCombinadaNinos10a18ResumenRegulares);
            Map<String, Integer> imcAdultosPlana = aplanarImcAdultos(imcAdultosResumen);
            Map<String, Integer> riesgosBiologicosPlana = aplanarRiesgosBiologicos(riesgosBiologicosResumen);
            fila.put("actividadesPlanas", actividadesPlanas);
            fila.put("antropometriaPlana", antropometriaPlana);
            fila.put("antropometriaRegularesPlana", antropometriaRegularesPlana);
            fila.put("antropometriaCombinadaNinos2a9Plana", antropometriaCombinadaNinos2a9Plana);
            fila.put("antropometriaCombinadaNinos2a9RegularesPlana", antropometriaCombinadaNinos2a9RegularesPlana);
            fila.put("antropometriaCombinadaNinos10a18Plana", antropometriaCombinadaNinos10a18Plana);
            fila.put("antropometriaCombinadaNinos10a18RegularesPlana", antropometriaCombinadaNinos10a18RegularesPlana);
            fila.put("imcAdultosPlana", imcAdultosPlana);
            fila.put("riesgosBiologicosPlana", riesgosBiologicosPlana);
            datosMes.add(fila);

            // Resumen aplanado por rango para acceso directo desde la plantilla
            mesPorInstitucion.put(ins.getId(), plano);
            actividadesPorInstitucion.put(ins.getId(), actividadesPlanas);
            antropometriaPorInstitucion.put(ins.getId(), antropometriaPlana);
            antropometriaRegularesPorInstitucion.put(ins.getId(), antropometriaRegularesPlana);
            antropometriaCombinadaNinos2a9PorInstitucion.put(ins.getId(), antropometriaCombinadaNinos2a9Plana);
            antropometriaCombinadaNinos2a9RegularesPorInstitucion.put(ins.getId(), antropometriaCombinadaNinos2a9RegularesPlana);
            antropometriaCombinadaNinos10a18PorInstitucion.put(ins.getId(), antropometriaCombinadaNinos10a18Plana);
            antropometriaCombinadaNinos10a18RegularesPorInstitucion.put(ins.getId(), antropometriaCombinadaNinos10a18RegularesPlana);
            imcAdultosPorInstitucion.put(ins.getId(), imcAdultosPlana);
            riesgosBiologicosPorInstitucion.put(ins.getId(), riesgosBiologicosPlana);
        }
        return new MesContext(datosMes, mesPorInstitucion, actividadesPorInstitucion, antropometriaPorInstitucion, antropometriaRegularesPorInstitucion, antropometriaCombinadaNinos2a9PorInstitucion, antropometriaCombinadaNinos2a9RegularesPorInstitucion, antropometriaCombinadaNinos10a18PorInstitucion, antropometriaCombinadaNinos10a18RegularesPorInstitucion, imcAdultosPorInstitucion, riesgosBiologicosPorInstitucion);
    }

    // Nuevo: método genérico para poblar el Context JXLS con una lista de meses
    // useActualMonthNumber = true para anual (mes1..mes12 usando número real de mes)
    // useActualMonthNumber = false para trimestral (mes1..mes3 indexados)
    private void fillContextForMonths(Context context, List<InstitucionDto> instituciones, int year, List<Integer> months, boolean useActualMonthNumber) {
        for (int i = 0; i < months.size(); i++) {
            int month = months.get(i);
            YearMonth ym = YearMonth.of(year, month);
            LocalDate fechaInicioMes = ym.atDay(1);
            LocalDate fechaFinMes = ym.atEndOfMonth();

            MesContext mc = calcularContextoMes(instituciones, fechaInicioMes, fechaFinMes);
            String keySuffix = useActualMonthNumber ? String.valueOf(month) : String.valueOf(i + 1);
            String baseKey = "mes" + keySuffix;
            context.putVar(baseKey, mc.lista());
            context.putVar(baseKey + "PorInstitucion", mc.porInstitucion());
            context.putVar(baseKey + "ActividadesPorInstitucion", mc.actividadesPorInstitucion());
            context.putVar(baseKey + "AntropometriaPorInstitucion", mc.antropometriaPorInstitucion());
            context.putVar(baseKey + "AntropometriaRegularesPorInstitucion", mc.antropometriaRegularesPorInstitucion());
            context.putVar(baseKey + "AntropometriaCombinadaNinos2a9PorInstitucion", mc.antropometriaCombinadaNinos2a9PorInstitucion());
            context.putVar(baseKey + "AntropometriaCombinadaNinos2a9RegularesPorInstitucion", mc.antropometriaCombinadaNinos2a9RegularesPorInstitucion());
            context.putVar(baseKey + "AntropometriaCombinadaNinos10a18PorInstitucion", mc.antropometriaCombinadaNinos10a18PorInstitucion());
            context.putVar(baseKey + "AntropometriaCombinadaNinos10a18RegularesPorInstitucion", mc.antropometriaCombinadaNinos10a18RegularesPorInstitucion());
            context.putVar(baseKey + "ImcAdultosPorInstitucion", mc.imcAdultosPorInstitucion());
            context.putVar(baseKey + "RiesgosBiologicosPorInstitucion", mc.riesgosBiologicosPorInstitucion());
        }
        log.info("Contexto JXLS poblado para {} meses del año {}", months.size(), year);
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

    // Nuevo: Aplana el resumen de IMC adultos por categoría y grupo etario
    private Map<String, Integer> aplanarImcAdultos(List<AntropometriaImcAdultosResumen> resumen) {
        Map<String, Integer> flat = new HashMap<>();
        // Inicializar claves esperadas para evitar nulls en la plantilla
        flat.put("imcAdultosObesidadMorbida19a59", 0);
        flat.put("imcAdultosObesidadMorbida60ymas", 0);
        flat.put("imcAdultosObesidad19a59", 0);
        flat.put("imcAdultosObesidad60ymas", 0);
        flat.put("imcAdultosSobrepeso19a59", 0);
        flat.put("imcAdultosSobrepeso60ymas", 0);
        flat.put("imcAdultosNormal19a59", 0);
        flat.put("imcAdultosNormal60ymas", 0);
        flat.put("imcAdultosDelgadezLeve19a59", 0);
        flat.put("imcAdultosDelgadezLeve60ymas", 0);
        flat.put("imcAdultosDelgadezModerada19a59", 0);
        flat.put("imcAdultosDelgadezModerada60ymas", 0);
        flat.put("imcAdultosDelgadezIntensa19a59", 0);
        flat.put("imcAdultosDelgadezIntensa60ymas", 0);
        flat.put("imcAdultosSinClasificacion19a59", 0);
        flat.put("imcAdultosSinClasificacion60ymas", 0);

        for (AntropometriaImcAdultosResumen a : resumen) {
            String categoria = a.categoria();
            String grupo = a.grupoEdad();
            Integer total = a.total() != null ? a.total() : 0;

            String sufijo = "19a59";
            if ("60+".equals(grupo)) {
                sufijo = "60ymas";
            }

            String base;
            switch (categoria) {
                case "Obesidad Mórbida (>40)" -> base = "imcAdultosObesidadMorbida";
                case "Obesidad (30,00 - 39,99)" -> base = "imcAdultosObesidad";
                case "Sobrepeso (25,00 - 29,99)" -> base = "imcAdultosSobrepeso";
                case "Normal (18,5 - 24,99)" -> base = "imcAdultosNormal";
                case "Delgadez Leve (17,00 - 18,49)" -> base = "imcAdultosDelgadezLeve";
                case "Delgadez Moderada (16,00 - 16,99)" -> base = "imcAdultosDelgadezModerada";
                case "Delgadez Intensa (<16,00)" -> base = "imcAdultosDelgadezIntensa";
                case "Sin Clasificación" -> base = "imcAdultosSinClasificacion";
                default -> base = "imcAdultosSinClasificacion";
            }
            String key = base + sufijo;
            flat.put(key, flat.getOrDefault(key, 0) + total);
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

    // Nuevo: obtener resumen IMC adultos por institución y rango de fechas
    private List<AntropometriaImcAdultosResumen> obtenerResumenImcAdultosPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenImcAdultosPorInstitucion(fechaInicio, fechaFin, institucionId);
        List<AntropometriaImcAdultosResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            String categoria = (String) row[0];
            String grupoEdad = (String) row[1];
            Integer total = toInteger(row[2]);
            result.add(new AntropometriaImcAdultosResumen(categoria, grupoEdad, total));
        }
        return result;
    }

    // Nuevo: obtener resumen de riesgos biológicos por institución y rango de fechas
    private List<RiesgoBiologicoResumen> obtenerResumenRiesgoBiologicoPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenRiesgoBiologicoPorInstitucion(fechaInicio, fechaFin, institucionId);
        List<RiesgoBiologicoResumen> result = new ArrayList<>();
        for (Object[] row : raw) {
            String nombre = (String) row[0];
            Integer total = toInteger(row[1]);
            result.add(new RiesgoBiologicoResumen(nombre, total));
        }
        return result;
    }

    private List<AntropometriaResumen> obtenerResumenAntropometriaCombinadaNinos10a18ConsultasRegularesPorInstitucion(LocalDate fechaInicio, LocalDate fechaFin, Integer institucionId) {
        List<Object[]> raw = reportsQueryRepository.obtenerResumenAntropometriaCombinadaNinos10a18ConsultasRegularesPorInstitucion(fechaInicio, fechaFin, institucionId);
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

    // Helper: Aplana el resumen de riesgos biológicos con claves estables
    private Map<String, Integer> aplanarRiesgosBiologicos(List<RiesgoBiologicoResumen> resumen) {
        Map<String, Integer> flat = new HashMap<>();
        // Inicializar claves conocidas en 0 para evitar nulls en la plantilla
        flat.put("riesgoBiologicoDesnutricion", 0);
        flat.put("riesgoBiologicoObesidad", 0);
        flat.put("riesgoBiologicoDiabetesMellitus", 0);
        flat.put("riesgoBiologicoEnfermedadCardioVascular", 0);
        flat.put("riesgoBiologicoEnfermedadHematopoyetica", 0);
        flat.put("riesgoBiologicoEnfermedadSistemaDigestivo", 0);
        flat.put("riesgoBiologicoEnfermedadInfecciosaParasitaria", 0);
        flat.put("riesgoBiologicoEnfermedadGenitalUrinaria", 0);
        flat.put("riesgoBiologicoEnfermedadPartoYPuerperio", 0);
        flat.put("riesgoBiologicoEnfermedadInmunologica", 0);
        flat.put("riesgoBiologicoEnfermedadReumatoidea", 0);
        flat.put("riesgoBiologicoEnfermedadViasRespiratorias", 0);
        flat.put("riesgoBiologicoDislipidemia", 0);
        flat.put("riesgoBiologicoOtros", 0);
        for (RiesgoBiologicoResumen r : resumen) {
            String key = mapNombreARiesgoKey(r.nombre());
            Integer total = r.total() != null ? r.total() : 0;
            flat.put(key, flat.getOrDefault(key, 0) + total);
        }
        return flat;
    }

    // Helper: Convierte nombres libres de riesgo a una clave camel case estable
    private String mapNombreARiesgoKey(String nombre) {
        String base = nombre == null ? "" : nombre;
        String normalized = java.text.Normalizer.normalize(base, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        normalized = normalized.replaceAll("[^A-Za-z0-9 ]", " ").trim();
        if (normalized.isEmpty()) {
            return "riesgoBiologicoSinNombre";
        }
        String[] parts = normalized.split("\\s+");
        StringBuilder sb = new StringBuilder("riesgoBiologico");
        for (String p : parts) {
            if (p.isEmpty()) continue;
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1).toLowerCase());
        }
        return sb.toString();
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

    public void generarReporteAnualPorMunicipio(Integer municipioSanitarioId, Integer anio, OutputStream os) throws Exception {
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

        String municipioSanitarioNombre = municipioSanitarioRepository.findById(municipioSanitarioId)
                .orElseThrow(() -> new RuntimeException("Municipio sanitario no encontrado con ID: " + municipioSanitarioId))
                .getNombre();

        try (InputStream is = getClass().getResourceAsStream("/templates/resumen_anual.xlsx")) {
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar la plantilla resumen_anual.xlsx");
            }
            Context context = new Context();
            context.putVar("instituciones", instituciones);
            context.putVar("anio", anio);
            context.putVar("municipioSanitarioNombre", municipioSanitarioNombre);

            // Poblar contexto con meses reales (mes1..mes12) usando número de mes como sufijo
            List<Integer> months = new ArrayList<>();
            for (int m = 1; m <= 12; m++) { months.add(m); }
            fillContextForMonths(context, instituciones, anio, months, true);

            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            jxlsHelper.setHideTemplateSheet(true);
            jxlsHelper.setDeleteTemplateSheet(true);
            Transformer transformer  = jxlsHelper.createTransformer(is, os);
            jxlsHelper.processTemplate(context, transformer);
            log.info("Reporte anual generado exitosamente en memoria");
        }
    }

    public void generarReporteTrimestralPorMunicipio(Integer municipioSanitarioId, Integer anio, Integer trimestre, OutputStream os) throws Exception {
        if (municipioSanitarioId == null || municipioSanitarioId <= 0) {
            log.warn("ID de municipio sanitario inválido: {}", municipioSanitarioId);
            throw new IllegalArgumentException("El ID del municipio sanitario debe ser un número positivo");
        }
        if (anio == null || anio < 1900 || anio > 2100) {
            log.warn("Año del reporte inválido: {}", anio);
            throw new IllegalArgumentException("El año del reporte debe ser un número válido entre 1900 y 2100");
        }
        if (trimestre == null || trimestre < 1 || trimestre > 4) {
            log.warn("Trimestre inválido: {}", trimestre);
            throw new IllegalArgumentException("El trimestre debe ser un número entre 1 y 4");
        }

        List<InstitucionDto> instituciones = institucionService.getInstitucionesByMunicipioSanitario(municipioSanitarioId);
        if (instituciones.isEmpty()) {
            log.warn("No se encontraron instituciones para el municipio sanitario ID: {}", municipioSanitarioId);
            throw new RuntimeException("No se encontraron instituciones para el municipio sanitario especificado");
        }

        String municipioSanitarioNombre = municipioSanitarioRepository.findById(municipioSanitarioId)
                .orElseThrow(() -> new RuntimeException("Municipio sanitario no encontrado con ID: " + municipioSanitarioId))
                .getNombre();

        // Determinar meses del trimestre
        List<Integer> months = new ArrayList<>();
        int startMonth = (trimestre - 1) * 3 + 1;
        for (int m = startMonth; m < startMonth + 3; m++) { months.add(m); }

        String templatePath = "/templates/resumen_trim_" + trimestre + ".xlsx";
        try (InputStream is = getClass().getResourceAsStream(templatePath)) {
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar la plantilla " + templatePath);
            }
            Context context = new Context();
            context.putVar("instituciones", instituciones);
            context.putVar("anio", anio);
            context.putVar("municipioSanitarioNombre", municipioSanitarioNombre);

            // Poblar contexto para los 3 meses del trimestre usando meses reales (mes4..mes6 según el trimestre)
            fillContextForMonths(context, instituciones, anio, months, true);

            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            jxlsHelper.setHideTemplateSheet(true);
            jxlsHelper.setDeleteTemplateSheet(true);
            Transformer transformer  = jxlsHelper.createTransformer(is, os);
            jxlsHelper.processTemplate(context, transformer);
            log.info("Reporte trimestral T{} generado exitosamente en memoria para municipio {}", trimestre, municipioSanitarioNombre);
        }
    }

    // Data Freshness: retorna JSON con la última actualización por usuario activo en cada institución del municipio
    public List<InstitucionDataFreshnessDto> obtenerDataFreshnessPorMunicipio(Integer municipioSanitarioId) {
        if (municipioSanitarioId == null || municipioSanitarioId <= 0) {
            throw new IllegalArgumentException("El ID del municipio sanitario debe ser un número positivo");
        }
        List<InstitucionDto> instituciones = institucionService.getInstitucionesByMunicipioSanitario(municipioSanitarioId);
        List<InstitucionDataFreshnessDto> resultado = new ArrayList<>();
        if (instituciones.isEmpty()) {
            return resultado;
        }
        for (InstitucionDto institucion : instituciones) {
            List<UsuarioInstitucion> usuariosActivos = usuarioInstitucionRepository.findActiveUsersByInstitucionId(institucion.getId());
            List<Integer> usuarioInstitucionIds = usuariosActivos.stream().map(UsuarioInstitucion::getId).toList();
            Map<Integer, LocalDateTime> ultimaActualizacionPorUi = new HashMap<>();
            if (!usuarioInstitucionIds.isEmpty()) {
                List<Object[]> raw = reportsQueryRepository.obtenerUltimaActualizacionPorUsuarioInstitucionIds(usuarioInstitucionIds);
                for (Object[] row : raw) {
                    Integer uiId = toInteger(row[0]);
                    LocalDateTime ts = toLocalDateTime(row[1]);
                    ultimaActualizacionPorUi.put(uiId, ts);
                }
            }
            List<UsuarioDataFreshnessDto> usuariosDto = new ArrayList<>();
            for (UsuarioInstitucion ui : usuariosActivos) {
                usuariosDto.add(
                    UsuarioDataFreshnessDto.builder()
                            .usuarioInstitucionId(ui.getId())
                            .usuarioId(ui.getUsuario().getId())
                            .cedula(ui.getUsuario().getCedula())
                            .nombres(ui.getUsuario().getNombres())
                            .apellidos(ui.getUsuario().getApellidos())
                            .rolNombre(ui.getRol() != null ? ui.getRol().getDescripcion() : null)
                            .ultimaActualizacion(ultimaActualizacionPorUi.get(ui.getId()))
                            .build()
                );
            }
            resultado.add(
                InstitucionDataFreshnessDto.builder()
                        .institucionId(institucion.getId())
                        .institucionNombre(institucion.getNombre())
                        .usuarios(usuariosDto)
                        .build()
            );
        }
        return resultado;
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) return null;
        if (value instanceof LocalDateTime ldt) return ldt;
        if (value instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
        if (value instanceof Date d) return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        try {
            return LocalDateTime.parse(value.toString());
        } catch (Exception e) {
            log.warn("No se pudo convertir a LocalDateTime: {}", value);
            return null;
        }
    }
}