package com.nutrizulia.catalog.service;

import com.nutrizulia.catalog.dto.MunicipioDto;

import java.util.List;

public interface IMunicipioService {

    List<MunicipioDto> getMunicipios(Integer idEstado);

}
