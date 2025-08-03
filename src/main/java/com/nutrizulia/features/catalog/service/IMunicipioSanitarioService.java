package com.nutrizulia.features.catalog.service;

import com.nutrizulia.features.catalog.dto.MunicipioSanitarioDto;

import java.util.List;

public interface IMunicipioSanitarioService {

    List<MunicipioSanitarioDto> getMunicipiosSanitarios(Integer idEstado);

}
