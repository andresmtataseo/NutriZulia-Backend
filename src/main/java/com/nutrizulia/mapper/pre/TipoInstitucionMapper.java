package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.TipoInstitucionDto;
import com.nutrizulia.model.pre.TipoInstitucion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoInstitucionMapper {

    TipoInstitucionDto toDto(TipoInstitucion tipoInstitucion);

}
