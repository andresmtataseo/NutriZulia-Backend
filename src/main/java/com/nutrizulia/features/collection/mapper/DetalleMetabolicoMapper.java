package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;
import com.nutrizulia.features.collection.model.DetalleMetabolico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleMetabolicoMapper {
    
    @Mapping(source = "consulta.id", target = "consulta_id")
    @Mapping(source = "glicemiaBasal", target = "glicemia_basal")
    @Mapping(source = "glicemaPostprandial", target = "glicemia_postprandial")
    @Mapping(source = "glicemiaAleatoria", target = "glicemia_aleatoria")
    @Mapping(source = "hemoglobinaGlicosilada", target = "hemoglobina_glicosilada")
    @Mapping(source = "trigliceridos", target = "trigliceridos")
    @Mapping(source = "colesterolTotal", target = "colesterol_total")
    @Mapping(source = "colesterolLdl", target = "colesterol_ldl")
    @Mapping(source = "colesterolHdl", target = "colesterol_hdl")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    DetalleMetabolicoDto toDto(DetalleMetabolico detalleMetabolico);
    
    @Mapping(source = "consulta_id", target = "consulta.id")
    @Mapping(source = "glicemia_basal", target = "glicemiaBasal")
    @Mapping(source = "glicemia_postprandial", target = "glicemaPostprandial")
    @Mapping(source = "glicemia_aleatoria", target = "glicemiaAleatoria")
    @Mapping(source = "hemoglobina_glicosilada", target = "hemoglobinaGlicosilada")
    @Mapping(source = "trigliceridos", target = "trigliceridos")
    @Mapping(source = "colesterol_total", target = "colesterolTotal")
    @Mapping(source = "colesterol_ldl", target = "colesterolLdl")
    @Mapping(source = "colesterol_hdl", target = "colesterolHdl")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    DetalleMetabolico toEntity(DetalleMetabolicoDto detalleMetabolicoDto);
}