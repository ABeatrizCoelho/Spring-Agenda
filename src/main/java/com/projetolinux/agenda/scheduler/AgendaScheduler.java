package com.projetolinux.agenda.scheduler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projetolinux.agenda.discord.DiscordBot;
import com.projetolinux.agenda.model.Agenda;
import com.projetolinux.agenda.repository.AgendaRepository;

@Component
public class AgendaScheduler {
    private final AgendaRepository agendaRepository;
    private final DiscordBot discordBot;


    public AgendaScheduler(AgendaRepository agendaRepository, DiscordBot discordBot) {
        this.agendaRepository = agendaRepository;
        this.discordBot = discordBot;
    }

    @Scheduled(fixedRate = 60000)
    public void verificarAgenda(){
        LocalDate hoje = LocalDate.now();
        LocalTime agora = LocalTime.now().withSecond(0).withNano(0);

        List<Agenda> compromissos = agendaRepository.findByData(hoje);


    /*olhar como salvar id do canal no banco de dados */
        compromissos.stream()
            .filter(a -> agora.equals(a.getHora()))
            .forEach(a -> {
                discordBot.enviarMensagen(
            "1447922015527764043", // teste de id do canal
            " **Lembrete da Agenda**\n" +
            "Descrição: " + a.getTitulo() + "\n" +
            "Horário: " + a.getHora() 
        );
    });



    }

    
}
