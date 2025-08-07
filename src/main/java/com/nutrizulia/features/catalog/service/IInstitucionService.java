package com.nutrizulia.features.catalog.service;

import com.nutrizulia.features.catalog.dto.InstitucionDto;

import java.util.List;

public interface IInstitucionService {

    List<InstitucionDto> getInstituciones();

    InstitucionDto getInstitucionesById(Integer id);

}
