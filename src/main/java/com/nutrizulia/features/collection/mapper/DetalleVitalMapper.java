package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.DetalleVitalDto;
import com.nutrizulia.features.collection.model.DetalleVital;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleVitalMapper {
    
    @Mapping(source = "consulta.id", target = "consulta_id")
    @Mapping(source = "tensionArterialSistolica", target = "tension_arterial_sistolica")
    @Mapping(source = "tensionArterialDiastolica", target = "tension_arterial_diastolica")
    @Mapping(source = "frecuenciaCardiaca", target = "frecuencia_cardiaca")
    @Mapping(source = "frecuenciaRespiratoria", target = "frecuencia_respiratoria")
    @Mapping(source = "temperatura", target = "temperatura")
    @Mapping(source = "saturacionOxigeno", target = "saturacion_oxigeno")
    @Mapping(source = "pulso", target = "pulso")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    DetalleVitalDto toDto(DetalleVital detalleVital);
    
    @Mapping(source = "consulta_id", target = "consulta.id")
    @Mapping(source = "tension_arterial_sistolica", target = "tensionArterialSistolica")
    @Mapping(source = "tension_arterial_diastolica", target = "tensionArterialDiastolica")
    @Mapping(source = "frecuencia_cardiaca", target = "frecuenciaCardiaca")
    @Mapping(source = "frecuencia_respiratoria", target = "frecuenciaRespiratoria")
    @Mapping(source = "temperatura", target = "temperatura")
    @Mapping(source = "saturacion_oxigeno", target = "saturacionOxigeno")
    @Mapping(source = "pulso", target = "pulso")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    DetalleVital toEntity(DetalleVitalDto detalleVitalDto);
}