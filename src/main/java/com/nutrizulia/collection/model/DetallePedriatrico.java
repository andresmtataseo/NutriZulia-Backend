package com.nutrizulia.collection.model;

import com.nutrizulia.common.entity.BaseEntity;
import com.nutrizulia.common.enums.TipoLactancia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "detalles_pedriaticos")
public class DetallePedriatrico extends BaseEntity {

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

}
