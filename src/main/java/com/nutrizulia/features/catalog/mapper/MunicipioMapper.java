package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.MunicipioDto;
import com.nutrizulia.features.catalog.model.Municipio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MunicipioMapper {

    @Mapping(source = "estado.id", target = "estado_id")
    MunicipioDto toDto(Municipio municipio);

}
