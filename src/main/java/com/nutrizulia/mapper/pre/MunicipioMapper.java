package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.MunicipioDto;
import com.nutrizulia.model.Municipio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MunicipioMapper {

    @Mapping(source = "estado.id", target = "estado_id")
    MunicipioDto toDto(Municipio municipio);

}
