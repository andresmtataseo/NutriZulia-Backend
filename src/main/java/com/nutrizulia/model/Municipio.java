    package com.nutrizulia.model;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import jakarta.persistence.Column;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.JoinColumn;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "municipios")
    public class Municipio {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "estado_id", nullable = false)
        private Estado estado;

        @Column(name = "nombre", nullable = false)
        private String nombre;

    }