package com.nutrizulia.catalog.service;

import com.nutrizulia.catalog.dto.MunicipioSanitarioDto;

import java.util.List;

public interface IMunicipioSanitarioService {

    List<MunicipioSanitarioDto> getMunicipiosSanitarios(Integer idEstado);

}
