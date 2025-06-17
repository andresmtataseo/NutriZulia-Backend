package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.ParametroCrecimientoPedriaticoEdadDto;
import com.nutrizulia.model.pre.ParametroCrecimientoPedriaticoEdad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParametroCrecimientoPedriaticoEdadMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "grupoEtario.id", target = "grupo_etario_id")
    @Mapping(source = "edadDia", target = "edad_dia")
    ParametroCrecimientoPedriaticoEdadDto toDto(ParametroCrecimientoPedriaticoEdad parametroCrecimientoPedriaticoEdad);

}
