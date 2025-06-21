package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.TipoActividadDto;
import com.nutrizulia.catalog.model.TipoActividad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoActividadMapper {

    TipoActividadDto toDto(TipoActividad tipoActividad);

}
