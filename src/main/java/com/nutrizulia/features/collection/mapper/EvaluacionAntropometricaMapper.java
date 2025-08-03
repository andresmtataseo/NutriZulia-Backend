package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.EvaluacionAntropometricaDto;
import com.nutrizulia.features.collection.model.EvaluacionAntropometrica;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EvaluacionAntropometricaMapper {
    
    @Mapping(source = "consulta.id", target = "consulta_id")
    @Mapping(source = "detalleAntropometrico.id", target = "detalle_antropometrico_id")
    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "valorCalculado", target = "valor_calculado")
    @Mapping(source = "tipoValorCalculado", target = "tipo_valor_calculado")
    @Mapping(source = "diagnosticoAntropometrico", target = "diagnostico_antropometrico")
    @Mapping(source = "fechaEvaluacion", target = "fecha_evaluacion")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    EvaluacionAntropometricaDto toDto(EvaluacionAntropometrica evaluacionAntropometrica);
    
    @Mapping(source = "consulta_id", target = "consulta.id")
    @Mapping(source = "detalle_antropometrico_id", target = "detalleAntropometrico.id")
    @Mapping(source = "tipo_indicador_id", target = "tipoIndicador.id")
    @Mapping(source = "valor_calculado", target = "valorCalculado")
    @Mapping(source = "tipo_valor_calculado", target = "tipoValorCalculado")
    @Mapping(source = "diagnostico_antropometrico", target = "diagnosticoAntropometrico")
    @Mapping(source = "fecha_evaluacion", target = "fechaEvaluacion")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    EvaluacionAntropometrica toEntity(EvaluacionAntropometricaDto evaluacionAntropometricaDto);
}