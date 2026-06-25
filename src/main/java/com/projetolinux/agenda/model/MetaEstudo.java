package com.projetolinux.agenda.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "meta_estudo")
@Data
public class MetaEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long discordUserId;

    @Column(nullable = false)
    private String materia;

    @Column(nullable = false)
    private Integer minutosPorDia;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private Boolean ativa = true;
}
