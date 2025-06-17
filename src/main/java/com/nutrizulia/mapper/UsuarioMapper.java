package com.nutrizulia.mapper;

import com.nutrizulia.dto.UsuarioDto;
import com.nutrizulia.model.admin.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(source = "isEnabled", target = "is_enabled")
    UsuarioDto toDto(Usuario usuario);
}
