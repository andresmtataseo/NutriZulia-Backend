package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.TipoIndicadorDto;
import com.nutrizulia.catalog.model.TipoIndicador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoIndicadorMapper {

    TipoIndicadorDto toDto(TipoIndicador tipoIndicador);

}
