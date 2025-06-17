package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.RiesgoBiologicoDto;
import com.nutrizulia.model.pre.RiesgoBiologico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiesgoBiologicoMapper {

    @Mapping(source = "edadMesMinima", target = "edad_mes_minima")
    @Mapping(source = "edadMesMaxima", target = "edad_mes_maxima")
    RiesgoBiologicoDto toDto(RiesgoBiologico riesgoBiologico);

}
