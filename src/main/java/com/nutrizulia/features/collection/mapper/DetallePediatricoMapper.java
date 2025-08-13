package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.DetallePedriatricoDto;
import com.nutrizulia.features.collection.model.DetallePediatrico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetallePediatricoMapper {

    @Mapping(source = "consulta.id", target = "consulta_id")
    @Mapping(source = "usaBiberon", target = "usa_biberon")
    @Mapping(source = "tipoLactancia", target = "tipo_lactancia")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    DetallePedriatricoDto toDto(DetallePediatrico detallePediatrico);
    
    @Mapping(source = "consulta_id", target = "consulta.id")
    @Mapping(source = "usa_biberon", target = "usaBiberon")
    @Mapping(source = "tipo_lactancia", target = "tipoLactancia")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    DetallePediatrico toEntity(DetallePedriatricoDto detallePedriatricoDto);
}