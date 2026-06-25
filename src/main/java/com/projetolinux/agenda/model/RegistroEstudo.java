package com.projetolinux.agenda.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "registro_estudo")
@Data
public class RegistroEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long discordUserId;

    @Column(nullable = false)
    private String materia;

    @Column(nullable = false)
    private Integer minutos;

    @Column(nullable = false)
    private LocalDate data;
}
