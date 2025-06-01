package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.NacionalidadDto;
import com.nutrizulia.model.Nacionalidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NacionalidadMapper {

    NacionalidadDto toDto(Nacionalidad nacionalidad);

}
