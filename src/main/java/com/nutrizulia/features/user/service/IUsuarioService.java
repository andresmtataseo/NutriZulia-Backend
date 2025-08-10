package com.nutrizulia.features.user.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDetallesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.user.model.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario findByCedula(String cedula);

    Usuario findById(Integer id);

    UsuarioDto saveUsuario(UsuarioDto usuarioDto);

    UsuarioDto createUsuario(UsuarioDto usuarioDto);

    void updatePassword(Integer idUsuario, String newPassword);

    PageResponseDto<UsuarioConInstitucionesDto> getUsuariosConInstituciones(int page, int size, String search, String sortBy, String sortDir);

    UsuarioDetallesDto getUsuarioDetalles(Integer idUsuario);

    boolean isCedulaAvailable(String cedula);

    boolean isEmailAvailable(String email);

    boolean isPhoneAvailable(String phone);

    void savePhone(Integer idUsuario, String telefono);

    void saveEmail(Integer idUsuario, String correo);

}
