package com.projetolinux.agenda.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

@Component
public class DiscordBot {

    @Value("${discord.bot.token}")
    private String token;

    private JDA jda;

    @PostConstruct
    public void start() throws Exception{
        jda = JDABuilder.createDefault(token).build();
    }

    public void enviarMensagen(String canalId, String mensagem){
        jda.getTextChannelById(canalId)
            .sendMessage(mensagem)
            .queue();
    }
    
}
