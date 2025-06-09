package com.nutrizulia.repository.pre;

import com.nutrizulia.model.pre.MunicipioSanitario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioSanitarioRepository extends JpaRepository<MunicipioSanitario, Integer> {
    List<MunicipioSanitario> findAllByEstado_Id(Integer estadoId);
}
