package com.nutrizulia.features.institution.service;

import com.nutrizulia.features.institution.dto.InstitucionDto;

import java.util.List;

public interface IInstitucionService {

    List<InstitucionDto> getInstituciones();

    InstitucionDto getInstitucionesById(Integer id);

}
