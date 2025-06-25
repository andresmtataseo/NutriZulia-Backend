package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.RegexDto;
import com.nutrizulia.catalog.model.Regex;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegexMapper {

    RegexDto toDto(Regex regex);

}
