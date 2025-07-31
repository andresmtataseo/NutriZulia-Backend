package com.nutrizulia.collection.mapper;

import com.nutrizulia.collection.dto.PacienteDto;
import com.nutrizulia.collection.model.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
    
    @Mapping(source = "usuarioInstitucion.id", target = "usuario_institucion_id")
    @Mapping(source = "fechaNacimiento", target = "fecha_nacimiento")
    @Mapping(source = "etnia.id", target = "etnia_id")
    @Mapping(source = "nacionalidad.id", target = "nacionalidad_id")
    @Mapping(source = "parroquia.id", target = "parroquia_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    PacienteDto toDto(Paciente paciente);
    
    @Mapping(source = "usuario_institucion_id", target = "usuarioInstitucion.id")
    @Mapping(source = "fecha_nacimiento", target = "fechaNacimiento")
    @Mapping(source = "etnia_id", target = "etnia.id")
    @Mapping(source = "nacionalidad_id", target = "nacionalidad.id")
    @Mapping(source = "parroquia_id", target = "parroquia.id")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    Paciente toEntity(PacienteDto pacienteDto);
}