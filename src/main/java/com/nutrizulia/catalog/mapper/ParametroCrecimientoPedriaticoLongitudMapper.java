package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.ParametroCrecimientoPedriaticoLongitudDto;
import com.nutrizulia.catalog.model.ParametroCrecimientoPedriaticoLongitud;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParametroCrecimientoPedriaticoLongitudMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "grupoEtario.id", target = "grupo_etario_id")
    @Mapping(source = "longitudCm", target = "longitud_cm")
    @Mapping(source = "tipoMedicion", target = "tipo_medicion")
    ParametroCrecimientoPedriaticoLongitudDto toDto(ParametroCrecimientoPedriaticoLongitud parametroCrecimientoPedriaticoLongitud);

}
