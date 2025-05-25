package com.nutrizulia.service;

import com.nutrizulia.model.Parroquia;

import java.util.List;

public interface IParroquiaService {

    public List<Parroquia> getParroquias(Integer idMunicipio, Integer idParroquia, String nombre);

    public Parroquia saveParroquia(Parroquia parroquia);

    public void deleteParroquia(Parroquia parroquia);

}
