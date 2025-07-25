package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.ReglaInterpretacionZScoreDto;
import com.nutrizulia.catalog.model.ReglaInterpretacionZScore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReglaInterpretacionZScoreMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "ZScoreMinimo", target = "z_score_minimo")
    @Mapping(source = "ZScoreMaximo", target = "z_score_maximo")
    ReglaInterpretacionZScoreDto toDto(ReglaInterpretacionZScore reglaInterpretacionZScore);

}