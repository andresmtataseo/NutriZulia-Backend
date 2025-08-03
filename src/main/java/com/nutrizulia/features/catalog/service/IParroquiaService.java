package com.nutrizulia.features.catalog.service;

import com.nutrizulia.features.catalog.dto.ParroquiaDto;

import java.util.List;

public interface IParroquiaService {

    List<ParroquiaDto> getParroquias(Integer idMunicipio);

}
