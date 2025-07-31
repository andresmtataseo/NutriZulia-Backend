package com.nutrizulia.collection.mapper;

import com.nutrizulia.collection.dto.PacienteRepresentanteDto;
import com.nutrizulia.collection.model.PacienteRepresentante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PacienteRepresentanteMapper {
    
    @Mapping(source = "usuarioInstitucion.id", target = "usuario_institucion_id")
    @Mapping(source = "paciente.id", target = "paciente_id")
    @Mapping(source = "representante.id", target = "representante_id")
    @Mapping(source = "parentesco.id", target = "parentesco_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    PacienteRepresentanteDto toDto(PacienteRepresentante pacienteRepresentante);
    
    @Mapping(source = "usuario_institucion_id", target = "usuarioInstitucion.id")
    @Mapping(source = "paciente_id", target = "paciente.id")
    @Mapping(source = "representante_id", target = "representante.id")
    @Mapping(source = "parentesco_id", target = "parentesco.id")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    PacienteRepresentante toEntity(PacienteRepresentanteDto pacienteRepresentanteDto);
}