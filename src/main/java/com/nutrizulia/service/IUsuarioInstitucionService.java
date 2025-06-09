package com.nutrizulia.service;

import com.nutrizulia.dto.UsuarioInstitucionDto;

import java.util.List;

public interface IUsuarioInstitucionService {

    List<UsuarioInstitucionDto> getInstitucionesByUsuarioId(Integer usuarioId);

}
