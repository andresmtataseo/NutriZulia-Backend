package com.nutrizulia.collection.mapper;

import com.nutrizulia.collection.dto.DetalleObstetriciaDto;
import com.nutrizulia.collection.model.DetalleObstetricia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleObstetriciaMapper {
    
    @Mapping(source = "consulta.id", target = "consulta_id")
    @Mapping(source = "estaEmbarazada", target = "esta_embarazada")
    @Mapping(source = "fechaUltimaMenstruacion", target = "fecha_ultima_menstruacion")
    @Mapping(source = "semanasGestacion", target = "semanas_gestacion")
    @Mapping(source = "pesoPreEmbarazo", target = "peso_pre_embarazo")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    DetalleObstetriciaDto toDto(DetalleObstetricia detalleObstetricia);
    
    @Mapping(source = "consulta_id", target = "consulta.id")
    @Mapping(source = "esta_embarazada", target = "estaEmbarazada")
    @Mapping(source = "fecha_ultima_menstruacion", target = "fechaUltimaMenstruacion")
    @Mapping(source = "semanas_gestacion", target = "semanasGestacion")
    @Mapping(source = "peso_pre_embarazo", target = "pesoPreEmbarazo")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    DetalleObstetricia toEntity(DetalleObstetriciaDto detalleObstetriciaDto);
}