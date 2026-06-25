package com.projetolinux.agenda.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetolinux.agenda.model.RegistroEstudo;

public interface RegistroEstudoRepository
        extends JpaRepository<RegistroEstudo, Long> {

    List<RegistroEstudo> findByDiscordUserId(Long discordUserId);

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
}