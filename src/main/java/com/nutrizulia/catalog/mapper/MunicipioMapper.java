package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.MunicipioDto;
import com.nutrizulia.catalog.model.Municipio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MunicipioMapper {

    @Mapping(source = "estado.id", target = "estado_id")
    MunicipioDto toDto(Municipio municipio);

}
