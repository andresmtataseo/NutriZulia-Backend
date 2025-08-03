package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.RiesgoBiologicoDto;
import com.nutrizulia.features.catalog.model.RiesgoBiologico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiesgoBiologicoMapper {

    @Mapping(source = "edadMesMinima", target = "edad_mes_minima")
    @Mapping(source = "edadMesMaxima", target = "edad_mes_maxima")
    RiesgoBiologicoDto toDto(RiesgoBiologico riesgoBiologico);

}
