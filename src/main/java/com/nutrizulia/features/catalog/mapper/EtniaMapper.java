package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.model.Etnia;
import com.nutrizulia.features.catalog.dto.EtniaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EtniaMapper {

    EtniaDto toDto(Etnia etnia);

}
