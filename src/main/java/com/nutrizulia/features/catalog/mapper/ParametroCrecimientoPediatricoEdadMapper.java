package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.ParametroCrecimientoPediatricoEdadDto;
import com.nutrizulia.features.catalog.model.ParametroCrecimientoPediatricoEdad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParametroCrecimientoPediatricoEdadMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "grupoEtario.id", target = "grupo_etario_id")
    @Mapping(source = "edadDia", target = "edad_dia")
    ParametroCrecimientoPediatricoEdadDto toDto(ParametroCrecimientoPediatricoEdad parametroCrecimientoPediatricoEdad);

}
