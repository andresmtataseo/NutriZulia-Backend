package com.nutrizulia.features.userinstitution.mapper;

import com.nutrizulia.features.userinstitution.dto.UsuarioInstitucionDto;
import com.nutrizulia.features.userinstitution.model.UsuarioInstitucion;
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
}
