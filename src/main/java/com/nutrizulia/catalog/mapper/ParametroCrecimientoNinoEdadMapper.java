package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.ParametroCrecimientoNinoEdadDto;
import com.nutrizulia.catalog.model.ParametroCrecimientoNinoEdad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParametroCrecimientoNinoEdadMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "grupoEtario.id", target = "grupo_etario_id")
    @Mapping(source = "edadMes", target = "edad_mes")
    ParametroCrecimientoNinoEdadDto toDto(ParametroCrecimientoNinoEdad parametroCrecimientoNinoEdad);

}
