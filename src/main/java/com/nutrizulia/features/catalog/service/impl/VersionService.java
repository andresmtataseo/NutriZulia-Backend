package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.features.catalog.dto.VersionDto;
import com.nutrizulia.features.catalog.mapper.VersionMapper;
import com.nutrizulia.features.catalog.model.Version;
import com.nutrizulia.features.catalog.repository.VersionRepository;
import com.nutrizulia.features.catalog.service.IVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VersionService implements IVersionService {

    private final VersionRepository versionRepository;
    private final VersionMapper versionMapper;

    @Override
    public List<VersionDto> getAll() {
        return versionRepository.findAll().stream().map(versionMapper::toDto).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void incrementarVersion(String nombreTabla) {
        log.debug("Incrementando versión para la tabla: {}", nombreTabla);
        
        try {
            Version version = versionRepository.findById(nombreTabla)
                    .orElse(new Version());
            
            if (version.getNombreTabla() == null) {
                // Es un nuevo registro
                version.setNombreTabla(nombreTabla);
                version.setVersion(1);
                log.info("Creando nueva entrada de versión para tabla '{}' con versión inicial: 1", nombreTabla);
            } else {
                // Incrementar versión existente
                version.setVersion(version.getVersion() + 1);
                log.info("Incrementando versión de tabla '{}' a: {}", nombreTabla, version.getVersion());
            }
            
            versionRepository.save(version);
            log.debug("Versión actualizada exitosamente para tabla: {}", nombreTabla);
            
        } catch (Exception e) {
            log.error("Error al incrementar versión para tabla '{}': {}", nombreTabla, e.getMessage(), e);
            throw new RuntimeException("Error al incrementar versión de tabla", e);
        }
    }
    
    @Override
    public Integer getVersionActual(String nombreTabla) {
        log.debug("Obteniendo versión actual para la tabla: {}", nombreTabla);
        
        try {
            return versionRepository.findById(nombreTabla)
                    .map(Version::getVersion)
                    .orElse(0);
        } catch (Exception e) {
            log.error("Error al obtener versión actual para tabla '{}': {}", nombreTabla, e.getMessage(), e);
            return 0;
        }
    }
}
