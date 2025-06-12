package com.nutrizulia.model;

import com.nutrizulia.enums.TipoLactancia;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "detalles_pedriaticos")
public class DetallePedriatrico {

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "usa_biberon")
    private Boolean usaBiberon;

    @Column(name = "tipo_lactancia")
    private TipoLactancia tipoLactancia ;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
