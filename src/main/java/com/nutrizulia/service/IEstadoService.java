package com.nutrizulia.service;

import com.nutrizulia.model.Estado;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IEstadoService {

    public List<Estado> getEstados(Integer idEstado, String nombre);

    public Estado saveEstado(Estado estado);

    public void deleteEstado(Estado estado);
}
