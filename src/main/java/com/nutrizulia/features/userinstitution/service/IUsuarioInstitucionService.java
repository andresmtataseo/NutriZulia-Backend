package com.nutrizulia.features.userinstitution.service;

import com.nutrizulia.features.userinstitution.dto.UsuarioInstitucionDto;

import java.util.List;

public interface IUsuarioInstitucionService {

    List<UsuarioInstitucionDto> getInstitucionesByUsuarioId(Integer usuarioId);

}
