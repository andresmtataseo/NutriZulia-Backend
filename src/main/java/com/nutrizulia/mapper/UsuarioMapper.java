package com.nutrizulia.mapper;

import com.nutrizulia.dto.UsuarioDto;
import com.nutrizulia.dto.UsuarioResponseDto;
import com.nutrizulia.dto.auth.SignInRequestDto;
import com.nutrizulia.dto.auth.SignUpRequestDto;
import com.nutrizulia.model.admin.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "isEnabled", ignore = true)
    Usuario toUser(SignUpRequestDto usuario);

    UsuarioResponseDto toUserResponseDto(Usuario usuario);

    @Mapping(source = "isEnabled", target = "is_enabled")
    UsuarioDto toDto(Usuario usuario);
}
