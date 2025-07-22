package com.nutrizulia.user.mapper;

import com.nutrizulia.user.dto.UsuarioDto;
import com.nutrizulia.user.dto.UsuarioResponseDto;
import com.nutrizulia.auth.dto.SignUpRequestDto;
import com.nutrizulia.user.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "isEnabled", ignore = true)
    Usuario toEntity(SignUpRequestDto usuario);

    UsuarioResponseDto toUserResponseDto(Usuario usuario);

    @Mapping(source = "isEnabled", target = "is_enabled")
    UsuarioDto toDto(Usuario usuario);
}
