package com.nutrizulia.features.user.service;

import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;

import java.util.List;

public interface IUsuarioInstitucionService {

    List<UsuarioInstitucionDto> getInstitucionesByUsuarioId(Integer usuarioId);

}
