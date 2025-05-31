package com.nutrizulia.service;

import com.nutrizulia.dto.pre.ParroquiaDto;

import java.util.List;

public interface IParroquiaService {

    List<ParroquiaDto> getParroquias(Integer idMunicipio);

}
