package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.GrupoEtarioDto;
import com.nutrizulia.model.pre.GrupoEtario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GrupoEtarioMapper {

    @Mapping(source = "edadMesMinima", target = "edad_mes_minima")
    @Mapping(source = "edadMesMaxima", target = "edad_mes_maxima")
    GrupoEtarioDto toDto(GrupoEtario grupoEtario);


}
