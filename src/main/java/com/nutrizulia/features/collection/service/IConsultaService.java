package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.ConsultaDto;

import java.util.List;

public interface IConsultaService {

    List<ConsultaDto> syncConsultas(List<ConsultaDto> consultas);

}
