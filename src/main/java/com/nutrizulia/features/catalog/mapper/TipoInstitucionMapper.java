package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.TipoInstitucionDto;
import com.nutrizulia.features.catalog.model.TipoInstitucion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoInstitucionMapper {

    TipoInstitucionDto toDto(TipoInstitucion tipoInstitucion);

}
