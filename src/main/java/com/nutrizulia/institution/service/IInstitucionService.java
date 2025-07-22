package com.nutrizulia.institution.service;

import com.nutrizulia.institution.dto.InstitucionDto;

import java.util.List;

public interface IInstitucionService {

    List<InstitucionDto> getInstituciones();

    InstitucionDto getInstitucionesById(Integer id);

}
