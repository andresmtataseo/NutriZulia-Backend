package com.nutrizulia.features.user.mapper;

import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.auth.dto.SignUpRequestDto;
import com.nutrizulia.features.user.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "isEnabled", ignore = true)
    Usuario toEntity(SignUpRequestDto usuario);

    @Mapping(source = "isEnabled", target = "is_enabled")
    UsuarioDto toDto(Usuario usuario);
}
