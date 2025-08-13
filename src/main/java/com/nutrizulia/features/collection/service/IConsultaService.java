package com.nutrizulia.features.collection.service;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.ConsultaDto;

import java.util.List;

public interface IConsultaService {

    BatchSyncResponseDTO syncConsultas(List<ConsultaDto> consultas);

}
