package com.nutrizulia.features.user.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.auth.dto.SignUpRequestDto;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.user.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<UsuarioDto> getUsuarios();

    UsuarioDto getUsuarioByCedula(String cedula);

    Optional<Usuario> findByCedulaWithRoles(String cedula);

    Usuario save(SignUpRequestDto signUpRequestDto);

    void updatePassword(Integer userId, String newEncodedPassword);

    PageResponseDto<UsuarioConInstitucionesDto> getUsuariosConInstituciones(
            int page,
            int size,
            String search,
            String sortBy,
            String sortDir
    );

}
