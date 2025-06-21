package com.nutrizulia.userinstitution.service;

import com.nutrizulia.userinstitution.dto.UsuarioInstitucionDto;

import java.util.List;

public interface IUsuarioInstitucionService {

    List<UsuarioInstitucionDto> getInstitucionesByUsuarioId(Integer usuarioId);

}
