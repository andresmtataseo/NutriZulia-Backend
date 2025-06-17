package com.nutrizulia.service;

import com.nutrizulia.dto.UsuarioDto;
import com.nutrizulia.model.admin.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<UsuarioDto> getUsuarios();

    UsuarioDto getUsuarioByCedula(String cedula);

    Optional<Usuario> findByCedulaWithRoles(String cedula);


}
