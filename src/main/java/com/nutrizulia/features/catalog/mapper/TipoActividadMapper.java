package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.TipoActividadDto;
import com.nutrizulia.features.catalog.model.TipoActividad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoActividadMapper {

    TipoActividadDto toDto(TipoActividad tipoActividad);

}
