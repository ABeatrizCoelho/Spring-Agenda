package com.projetolinux.agenda.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projetolinux.agenda.model.RegistroEstudo;

public interface RegistroEstudoRepository extends JpaRepository<RegistroEstudo, Long> {


    @Query("""
            SELECT COALESCE(SUM(r.minutos), 0)
            FROM RegistroEstudo r
            WHERE r.discordUserId = :discordUserId
              AND r.materia = :materia
              AND r.data = :data
            """)
    Integer totalEstudadoHoje(
            @Param("discordUserId") Long discordUserId,
            @Param("materia") String materia,
            @Param("data") LocalDate data);

    @Query("""
            SELECT COALESCE(SUM(r.minutos), 0)
            FROM RegistroEstudo r
            WHERE r.discordUserId = :discordUserId
              AND r.data = :data
            """)
    Integer totalEstudadoHoje(
            @Param("discordUserId") Long discordUserId,
            @Param("data") LocalDate data);


    List<RegistroEstudo> findByDiscordUserId(Long discordUserId);


    List<RegistroEstudo> findByDiscordUserIdOrderByDataDesc(Long discordUserId);

    List<RegistroEstudo> findByDiscordUserIdAndData(
            Long discordUserId,
            LocalDate data);


    List<RegistroEstudo> findByDiscordUserIdAndMateria(
            Long discordUserId,
            String materia);


    List<RegistroEstudo> findByDiscordUserIdAndMateriaAndData(
            Long discordUserId,
            String materia,
            LocalDate data);


    List<RegistroEstudo> findByDiscordUserIdAndMateriaOrderByDataDesc(
            Long discordUserId,
            String materia);
}