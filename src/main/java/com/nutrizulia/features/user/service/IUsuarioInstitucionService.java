package com.nutrizulia.features.user.service;

import com.nutrizulia.features.user.dto.CreateUsuarioInstitucionDto;
import com.nutrizulia.features.user.dto.UpdateUsuarioInstitucionDto;
import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;

import java.util.List;

/**
 * Interfaz del servicio para gestionar las asignaciones de usuarios a instituciones.
 * Define las operaciones disponibles para crear, consultar y gestionar las relaciones
 * entre usuarios, instituciones y roles.
 */
public interface IUsuarioInstitucionService {

    UsuarioInstitucionDto createUsuarioInstitucion(CreateUsuarioInstitucionDto createDto);

    UsuarioInstitucionDto updateUsuarioInstitucion(Integer id, UpdateUsuarioInstitucionDto updateDto);

    List<UsuarioInstitucionDto> getUsuarioInstitucionesByUsuarioId(Integer usuarioId);

}
