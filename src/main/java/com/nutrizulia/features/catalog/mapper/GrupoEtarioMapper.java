package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.GrupoEtarioDto;
import com.nutrizulia.features.catalog.model.GrupoEtario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GrupoEtarioMapper {

    @Mapping(source = "edadMesMinima", target = "edad_mes_minima")
    @Mapping(source = "edadMesMaxima", target = "edad_mes_maxima")
    GrupoEtarioDto toDto(GrupoEtario grupoEtario);


}
