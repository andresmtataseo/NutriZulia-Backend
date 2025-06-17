package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.ReglaInterpretacionImcDto;
import com.nutrizulia.model.pre.ReglaInterpretacionImc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReglaInterpretacionImcMapper {

    @Mapping(source = "tipoIndicador.id", target = "tipo_indicador_id")
    @Mapping(source = "imcMinimo", target = "imc_minimo")
    @Mapping(source = "imcMaximo", target = "imc_maximo")
    ReglaInterpretacionImcDto toDto(ReglaInterpretacionImc reglaInterpretacionImc);

}