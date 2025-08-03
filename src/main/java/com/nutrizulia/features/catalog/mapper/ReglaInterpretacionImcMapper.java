package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.ReglaInterpretacionImcDto;
import com.nutrizulia.features.catalog.model.ReglaInterpretacionImc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReglaInterpretacionImcMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "imcMinimo", target = "imc_minimo")
    @Mapping(source = "imcMaximo", target = "imc_maximo")
    ReglaInterpretacionImcDto toDto(ReglaInterpretacionImc reglaInterpretacionImc);

}