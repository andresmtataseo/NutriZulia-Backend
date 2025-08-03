package com.nutrizulia.features.catalog.service;

import com.nutrizulia.features.catalog.dto.MunicipioDto;

import java.util.List;

public interface IMunicipioService {

    List<MunicipioDto> getMunicipios(Integer idEstado);

}
