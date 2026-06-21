package com.projetolinux.agenda.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projetolinux.agenda.model.Agenda;
import com.projetolinux.agenda.repository.AgendaRepository;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Agenda criarAgenda(Long discordUserId,
            LocalDate data,
            LocalTime hora,
            String titulo) {

        Agenda agenda = new Agenda();
        agenda.setDiscordUserId(discordUserId);
        agenda.setData(data);
        agenda.setHora(hora);
        agenda.setTitulo(titulo);

        return agendaRepository.save(agenda);
    }

    public List<Agenda> listarHoje(Long discordUserId, LocalDate data) {
        return agendaRepository.findByDiscordUserIdAndDataOrderByHora(
                discordUserId,
                data);
    }

    public boolean apagarAgenda(Long discordUserId, Long id) {

        Agenda agenda = agendaRepository
                .findByIdAndDiscordUserId(discordUserId, id)
                .orElse(null);

        if (agenda == null) {
            return false;
        }

        agendaRepository.delete(agenda);
        return true;
    }
}