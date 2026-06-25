package com.projetolinux.agenda.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetolinux.agenda.model.MetaEstudo;

public interface MetaEstudoRepository
        extends JpaRepository<MetaEstudo, Long> {

    List<MetaEstudo> findByDiscordUserId(Long discordUserId);

    Optional<MetaEstudo> findByDiscordUserIdAndMateria(
            Long discordUserId,
            String materia);

}