package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.RegexDto;
import com.nutrizulia.features.catalog.model.Regex;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegexMapper {

    RegexDto toDto(Regex regex);

}
