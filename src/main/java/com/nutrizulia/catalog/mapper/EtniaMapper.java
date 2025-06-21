package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.model.Etnia;
import com.nutrizulia.catalog.dto.EtniaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EtniaMapper {

    EtniaDto toDto(Etnia etnia);

}
