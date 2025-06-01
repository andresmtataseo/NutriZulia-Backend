package com.nutrizulia.service.pre;

import com.nutrizulia.dto.pre.MunicipioDto;

import java.util.List;

public interface IMunicipioService {

    List<MunicipioDto> getMunicipios(Integer idEstado);

}
