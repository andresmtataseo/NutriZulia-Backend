package com.nutrizulia.features.catalog.service;

import com.nutrizulia.features.catalog.dto.RolDto;

import java.util.List;

public interface IRolService {

    List<RolDto> getRoles();

    RolDto getRolById(Integer id);

}
