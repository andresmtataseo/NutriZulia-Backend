package com.nutrizulia.collection.mapper;

import com.nutrizulia.collection.dto.DiagnosticoDto;
import com.nutrizulia.collection.model.Diagnostico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiagnosticoMapper {
    
    @Mapping(source = "consulta.id", target = "consulta_id")
    @Mapping(source = "riesgoBiologico.id", target = "riesgo_biologico_id")
    @Mapping(source = "enfermedad.id", target = "enfermedad_id")
    @Mapping(source = "isPrincipal", target = "is_principal")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    DiagnosticoDto toDto(Diagnostico diagnostico);
    
    @Mapping(source = "consulta_id", target = "consulta.id")
    @Mapping(source = "riesgo_biologico_id", target = "riesgoBiologico.id")
    @Mapping(source = "enfermedad_id", target = "enfermedad.id")
    @Mapping(source = "is_principal", target = "isPrincipal")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    Diagnostico toEntity(DiagnosticoDto diagnosticoDto);
}