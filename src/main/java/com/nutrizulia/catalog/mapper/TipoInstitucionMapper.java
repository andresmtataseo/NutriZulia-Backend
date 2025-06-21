package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.TipoInstitucionDto;
import com.nutrizulia.catalog.model.TipoInstitucion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoInstitucionMapper {

    TipoInstitucionDto toDto(TipoInstitucion tipoInstitucion);

}
