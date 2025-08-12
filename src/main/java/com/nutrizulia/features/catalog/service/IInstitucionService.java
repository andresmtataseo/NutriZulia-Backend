package com.nutrizulia.features.catalog.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.catalog.dto.InstitucionConUsuariosDto;
import com.nutrizulia.features.catalog.dto.InstitucionDto;

import java.util.List;

public interface IInstitucionService {

    List<InstitucionDto> getInstituciones();

    InstitucionDto getInstitucionesById(Integer id);

    PageResponseDto<InstitucionConUsuariosDto> getInstitucionConUsuarios(int page, int size, String search, String sortBy, String sortDir);

}
