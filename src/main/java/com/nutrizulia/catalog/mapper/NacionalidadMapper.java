package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.NacionalidadDto;
import com.nutrizulia.catalog.model.Nacionalidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NacionalidadMapper {

    NacionalidadDto toDto(Nacionalidad nacionalidad);

}
