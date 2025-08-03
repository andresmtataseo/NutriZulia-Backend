package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.NacionalidadDto;
import com.nutrizulia.features.catalog.model.Nacionalidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NacionalidadMapper {

    NacionalidadDto toDto(Nacionalidad nacionalidad);

}
