package com.nutrizulia.catalog.service;

import com.nutrizulia.catalog.dto.ParroquiaDto;

import java.util.List;

public interface IParroquiaService {

    List<ParroquiaDto> getParroquias(Integer idMunicipio);

}
