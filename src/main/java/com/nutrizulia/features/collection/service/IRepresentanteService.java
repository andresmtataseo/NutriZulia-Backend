package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.RepresentanteDto;

import java.util.List;

public interface IRepresentanteService {
    List<RepresentanteDto> syncRepresentantes(List<RepresentanteDto> representantes);
}
