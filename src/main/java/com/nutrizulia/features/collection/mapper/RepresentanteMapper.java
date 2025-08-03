package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.RepresentanteDto;
import com.nutrizulia.features.collection.model.Representante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepresentanteMapper {
    
    @Mapping(source = "usuarioInstitucion.id", target = "usuario_institucion_id")
    @Mapping(source = "fechaNacimiento", target = "fecha_nacimiento")
    @Mapping(source = "etnia.id", target = "etnia_id")
    @Mapping(source = "nacionalidad.id", target = "nacionalidad_id")
    @Mapping(source = "parroquia.id", target = "parroquia_id")
    @Mapping(source = "cedula", target = "cedula")
    @Mapping(source = "nombres", target = "nombres")
    @Mapping(source = "apellidos", target = "apellidos")
    @Mapping(source = "genero", target = "genero")
    @Mapping(source = "domicilio", target = "domicilio")
    @Mapping(source = "telefono", target = "telefono")
    @Mapping(source = "correo", target = "correo")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    RepresentanteDto toDto(Representante representante);
    
    @Mapping(source = "usuario_institucion_id", target = "usuarioInstitucion.id")
    @Mapping(source = "fecha_nacimiento", target = "fechaNacimiento")
    @Mapping(source = "etnia_id", target = "etnia.id")
    @Mapping(source = "nacionalidad_id", target = "nacionalidad.id")
    @Mapping(source = "parroquia_id", target = "parroquia.id")
    @Mapping(source = "cedula", target = "cedula")
    @Mapping(source = "nombres", target = "nombres")
    @Mapping(source = "apellidos", target = "apellidos")
    @Mapping(source = "genero", target = "genero")
    @Mapping(source = "domicilio", target = "domicilio")
    @Mapping(source = "telefono", target = "telefono")
    @Mapping(source = "correo", target = "correo")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    Representante toEntity(RepresentanteDto representanteDto);
}