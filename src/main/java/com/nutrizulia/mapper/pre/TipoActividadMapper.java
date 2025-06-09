package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.TipoActividadDto;
import com.nutrizulia.model.pre.TipoActividad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoActividadMapper {

    TipoActividadDto toDto(TipoActividad tipoActividad);

}
