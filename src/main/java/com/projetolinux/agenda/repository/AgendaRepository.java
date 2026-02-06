package com.projetolinux.agenda.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetolinux.agenda.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByData(LocalDate data);

    List<Agenda> findByTituloContaining(String titulo);

    List<Agenda> findByDataOrderByHora(LocalDate data);


}
