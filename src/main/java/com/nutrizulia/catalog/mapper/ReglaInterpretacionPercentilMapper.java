package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.ReglaInterpretacionPercentilDto;
import com.nutrizulia.catalog.model.ReglaInterpretacionPercentil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReglaInterpretacionPercentilMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "percentilMinimo", target = "percentil_minimo")
    @Mapping(source = "percentilMaximo", target = "percentil_maximo")
    ReglaInterpretacionPercentilDto toDto(ReglaInterpretacionPercentil reglaInterpretacionPercentil);

}
