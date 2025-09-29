package com.nutrizulia.features.reports.service;

import com.nutrizulia.features.catalog.dto.InstitucionDto;
import com.nutrizulia.features.catalog.service.impl.InstitucionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jxls.transform.Transformer;
import org.springframework.stereotype.Service;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteService {

    private final InstitucionService institucionService;

    public void generarReporteTrimestralPorMunicipio(Integer municipioSanitarioId, OutputStream os) throws Exception {
        log.info("Generando reporte trimestral para municipio sanitario ID: {}", municipioSanitarioId);
        
        if (municipioSanitarioId == null || municipioSanitarioId <= 0) {
            log.warn("ID de municipio sanitario inválido: {}", municipioSanitarioId);
            throw new IllegalArgumentException("El ID del municipio sanitario debe ser un número positivo");
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
            // Pasar los mismos datos a todas las hojas del trimestre
            context.putVar("instituciones", instituciones);
            context.putVar("trimestre1", instituciones);
            context.putVar("trimestre2", instituciones);
            context.putVar("trimestre3", instituciones);
            context.putVar("trimestre4", instituciones);
            
            // Debug: Verificar el contexto JXLS
            log.info("Contexto JXLS creado con {} instituciones para todas las hojas del reporte", instituciones.size());
            
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