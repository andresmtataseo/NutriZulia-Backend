package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.TipoIndicadorDto;
import com.nutrizulia.features.catalog.model.TipoIndicador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoIndicadorMapper {

    TipoIndicadorDto toDto(TipoIndicador tipoIndicador);

}
