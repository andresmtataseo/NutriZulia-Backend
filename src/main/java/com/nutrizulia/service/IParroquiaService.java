package com.nutrizulia.service;

import com.nutrizulia.model.Parroquia;

import java.util.List;

public interface IParroquiaService {

    List<Parroquia> getParroquias(Integer idMunicipio, Integer idParroquia, String nombre);

    Parroquia getParroquiaById(Integer idParroquia);

    Parroquia saveParroquia(Parroquia parroquia);

    void deleteParroquia(Parroquia parroquia);

}
