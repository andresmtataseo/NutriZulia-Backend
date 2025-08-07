package com.nutrizulia.features.user.mapper;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.user.dto.InstitucionRolDto;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioConInstitucionesMapper {

    @Mapping(target = "instituciones", source = "usuarioInstituciones")
    UsuarioConInstitucionesDto toDto(Usuario usuario);

    @Mapping(target = "institucionId", source = "institucion.id")
    @Mapping(target = "institucionNombre", source = "institucion.nombre")
    @Mapping(target = "rolId", source = "rol.id")
    @Mapping(target = "rolNombre", source = "rol.descripcion")
    InstitucionRolDto toInstitucionRolDto(UsuarioInstitucion usuarioInstitucion);

    List<UsuarioConInstitucionesDto> toDtoList(List<Usuario> usuarios);

    default PageResponseDto<UsuarioConInstitucionesDto> toPageDto(Page<Usuario> page) {
        PageResponseDto<UsuarioConInstitucionesDto> response = new PageResponseDto<>();
        response.setContent(toDtoList(page.getContent()));
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        response.setEmpty(page.isEmpty());
        return response;
    }
}