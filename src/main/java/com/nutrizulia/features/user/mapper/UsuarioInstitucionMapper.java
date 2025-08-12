package com.nutrizulia.features.user.mapper;

import com.nutrizulia.features.user.dto.CreateUsuarioInstitucionDto;
import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioInstitucionMapper {
    @Mapping(source = "institucion.id", target = "institucion_id")
    @Mapping(source = "usuario.id", target = "usuario_id")
    @Mapping(source = "rol.id", target = "rol_id")
    @Mapping(source = "fechaInicio", target = "fecha_inicio")
    @Mapping(source = "fechaFin", target = "fecha_fin")
    @Mapping(source = "isEnabled", target = "is_enabled")
    UsuarioInstitucionDto toDto(UsuarioInstitucion usuarioInstitucion);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "usuario_id", target = "usuario.id")
    @Mapping(source = "institucion_id", target = "institucion.id")
    @Mapping(source = "rol_id", target = "rol.id")
    @Mapping(target = "fechaInicio", ignore = true)
    @Mapping(target = "fechaFin", ignore = true)
    @Mapping(source = "is_enabled", target = "isEnabled")
    UsuarioInstitucion toEntity(CreateUsuarioInstitucionDto createDto);
}
