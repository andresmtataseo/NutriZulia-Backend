package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.catalog.dto.InstitucionConUsuariosDto;
import com.nutrizulia.features.catalog.model.Institucion;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitucionConUsuariosMapper {

    @Mapping(target = "municipio_sanitario.id", source = "institucion.municipioSanitario.id")
    @Mapping(target = "municipio_sanitario.nombre", source = "institucion.municipioSanitario.nombre")
    @Mapping(target = "tipo_institucion.id", source = "institucion.tipoInstitucion.id")
    @Mapping(target = "tipo_institucion.nombre", source = "institucion.tipoInstitucion.nombre")
    @Mapping(target = "usuarios", source = "usuariosConRoles")
    InstitucionConUsuariosDto toDto(Institucion institucion, List<InstitucionConUsuariosDto.UsuarioConRolDto> usuariosConRoles);

    @Mapping(target = "id", source = "usuario.id")
    @Mapping(target = "cedula", source = "usuario.cedula")
    @Mapping(target = "nombres", source = "usuario.nombres")
    @Mapping(target = "apellidos", source = "usuario.apellidos")
    @Mapping(target = "fecha_nacimiento", source = "usuario.fechaNacimiento")
    @Mapping(target = "genero", source = "usuario.genero")
    @Mapping(target = "telefono", source = "usuario.telefono")
    @Mapping(target = "correo", source = "usuario.correo")
    @Mapping(target = "is_enabled", source = "usuario.isEnabled")
    @Mapping(target = "usuario_institucion.id", source = "usuarioInstitucion.id")
    @Mapping(target = "usuario_institucion.institucion_id", source = "usuarioInstitucion.institucion.id")
    @Mapping(target = "usuario_institucion.rol.id", source = "usuarioInstitucion.rol.id")
    @Mapping(target = "usuario_institucion.rol.nombre", source = "usuarioInstitucion.rol.nombre")
    @Mapping(target = "usuario_institucion.rol.descripcion", source = "usuarioInstitucion.rol.descripcion")
    @Mapping(target = "usuario_institucion.fecha_inicio", source = "usuarioInstitucion.fechaInicio")
    @Mapping(target = "usuario_institucion.fecha_fin", source = "usuarioInstitucion.fechaFin")
    @Mapping(target = "usuario_institucion.is_enabled", source = "usuarioInstitucion.isEnabled")
    InstitucionConUsuariosDto.UsuarioConRolDto toUsuarioConRolDto(Usuario usuario, UsuarioInstitucion usuarioInstitucion);

    List<InstitucionConUsuariosDto> toDtoList(List<Institucion> instituciones);

    default PageResponseDto<InstitucionConUsuariosDto> toPageDto(Page<InstitucionConUsuariosDto> page) {
        return PageResponseDto.<InstitucionConUsuariosDto>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}