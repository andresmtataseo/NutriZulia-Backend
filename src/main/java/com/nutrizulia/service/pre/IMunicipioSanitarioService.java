package com.nutrizulia.service.pre;

import com.nutrizulia.dto.pre.MunicipioSanitarioDto;

import java.util.List;

public interface IMunicipioSanitarioService {

    List<MunicipioSanitarioDto> getMunicipiosSanitarios(Integer idEstado);

}
