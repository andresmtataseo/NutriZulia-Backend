package com.nutrizulia.features.user.service;

import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;
import com.nutrizulia.features.user.model.UsuarioInstitucion;

import java.util.List;
import java.util.Optional;

public interface IUsuarioInstitucionService {

    List<UsuarioInstitucionDto> getInstitucionesByUsuarioId(Integer usuarioId);

    Optional<UsuarioInstitucion> getUsuarioAdmin(Integer usuarioId);

}
