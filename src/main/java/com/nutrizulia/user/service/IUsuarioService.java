package com.nutrizulia.user.service;

import com.nutrizulia.user.dto.UsuarioDto;
import com.nutrizulia.user.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<UsuarioDto> getUsuarios();

    UsuarioDto getUsuarioByCedula(String cedula);

    Optional<Usuario> findByCedulaWithRoles(String cedula);


}
