package com.projetolinux.agenda.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

@Component
public class DiscordBot {

    @Value("${discord.bot.token}")
    private String token;

    private JDA jda;

    private final DiscordListener discordListener;

    

    public DiscordBot(DiscordListener discordListener) {
        this.discordListener = discordListener;
    }

    @PostConstruct
    public void start() throws Exception{
        jda = JDABuilder.createDefault(token)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .addEventListeners(discordListener)
        .build();
    }

    public void enviarMensagen(String canalId, String mensagem){
        jda.getTextChannelById(canalId)
            .sendMessage(mensagem)
            .queue();
    }
    
}
