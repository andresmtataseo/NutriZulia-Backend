package com.nutrizulia.features.catalog.service;

import com.nutrizulia.features.catalog.dto.VersionDto;

import java.util.List;

public interface IVersionService {
    List<VersionDto> getAll();
    
    /**
     * Incrementa la versión de una tabla específica
     * @param nombreTabla nombre de la tabla cuya versión se incrementará
     */
    void incrementarVersion(String nombreTabla);
    
    /**
     * Obtiene la versión actual de una tabla
     * @param nombreTabla nombre de la tabla
     * @return la versión actual o 0 si no existe
     */
    Integer getVersionActual(String nombreTabla);
}
