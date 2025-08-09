package com.nutrizulia.features.user.mapper;

import com.nutrizulia.features.catalog.mapper.InstitucionMapper;
import com.nutrizulia.features.catalog.mapper.RolMapper;
import com.nutrizulia.features.user.dto.UsuarioDetallesDto;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {InstitucionMapper.class, RolMapper.class})
public interface UsuarioDetallesMapper {

    @Mapping(target = "instituciones", source = "usuarioInstituciones")
    UsuarioDetallesDto toDto(Usuario usuario);

    @Mapping(target = "institucion", source = "institucion")
    @Mapping(target = "rol", source = "rol")
    UsuarioDetallesDto.UsuarioInstitucionDetalleDto toUsuarioInstitucionDetalleDto(UsuarioInstitucion usuarioInstitucion);
}