package com.nutrizulia.features.user.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.user.model.Usuario;

import java.util.List;

public interface IUsuarioService {

    List<UsuarioDto> getUsuarios();

    Usuario findByCedula(String cedula);

    UsuarioDto saveUsuario(UsuarioDto usuarioDto);

    UsuarioDto createUsuario(UsuarioDto usuarioDto);

    void updatePassword(String cedula, String newPassword);

    PageResponseDto<UsuarioConInstitucionesDto> getUsuariosConInstituciones(int page, int size, String search, String sortBy, String sortDir);

    boolean isCedulaAvailable(String cedula);

    boolean isEmailAvailable(String email);

    boolean isPhoneAvailable(String phone);

}
