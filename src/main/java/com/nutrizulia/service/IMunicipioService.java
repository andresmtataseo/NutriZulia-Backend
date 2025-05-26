package com.nutrizulia.service;

import com.nutrizulia.model.Municipio;

import java.util.List;

public interface IMunicipioService {

    public List<Municipio> getMunicipios(Integer idEstado, Integer idMunicipio, String nombre);

    public Municipio getMunicipioById(Integer idMunicipio);

    public Municipio saveMunicipio(Municipio municipio);

    public void deleteMunicipio(Municipio municipio);

}
