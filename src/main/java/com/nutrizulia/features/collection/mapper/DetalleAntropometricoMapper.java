package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.DetalleAntropometricoDto;
import com.nutrizulia.features.collection.model.DetalleAntropometrico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleAntropometricoMapper {
    
    @Mapping(source = "consulta.id", target = "consulta_id")
    @Mapping(source = "circuferenciaBraquial", target = "circuferencia_braquial")
    @Mapping(source = "circuferenciaCadera", target = "circuferencia_cadera")
    @Mapping(source = "circuferenciaCintura", target = "circuferencia_cintura")
    @Mapping(source = "perimetroCefalico", target = "perimetro_cefalico")
    @Mapping(source = "pliegueTricipital", target = "pliegue_tricipital")
    @Mapping(source = "pliegueSubescapular", target = "pliegue_subescapular")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    DetalleAntropometricoDto toDto(DetalleAntropometrico detalleAntropometrico);
    
    @Mapping(source = "consulta_id", target = "consulta.id")
    @Mapping(source = "circuferencia_braquial", target = "circuferenciaBraquial")
    @Mapping(source = "circuferencia_cadera", target = "circuferenciaCadera")
    @Mapping(source = "circuferencia_cintura", target = "circuferenciaCintura")
    @Mapping(source = "perimetro_cefalico", target = "perimetroCefalico")
    @Mapping(source = "pliegue_tricipital", target = "pliegueTricipital")
    @Mapping(source = "pliegue_subescapular", target = "pliegueSubescapular")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    DetalleAntropometrico toEntity(DetalleAntropometricoDto detalleAntropometricoDto);
}