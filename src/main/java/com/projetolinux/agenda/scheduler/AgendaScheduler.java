package com.projetolinux.agenda.scheduler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.
stereotype.Component;

import com.projetolinux.agenda.discord.DiscordBot;
import com.projetolinux.agenda.model.Agenda;
import com.projetolinux.agenda.repository.AgendaRepository;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
        System.out.println("Scheduler executando: " + LocalTime.now());
        LocalDate hoje = LocalDate.now(
                ZoneId.of("America/Sao_Paulo")
        );
        LocalTime agora = LocalTime.now(
                ZoneId.of("America/Sao_Paulo")
        ).withSecond(0).withNano(0);

        System.out.println("Agora: " + agora);

        
        List<Agenda> compromissos = agendaRepository.findByData(hoje);



    /*olhar como salvar id do canal no banco de dados */
        compromissos.stream()
            .filter(a -> agora.equals(a.getHora()))
            .forEach(a -> {
                discordBot.enviarMensagem(
            a.getDiscordUserId(), // teste de id do canal
                        "⏰ Lembrete\n\n" +
                                "📌 " + a.getTitulo() + "\n" +
                                "🕒 " + a.getHora() + "\n" +
                                "📅 " + a.getData()
        );
    });



    }

    
}
