package com.projetolinux.agenda.discord;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
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

        jda.awaitReady();

        jda.updateCommands()
                .addCommands(
                        Commands.slash("agenda", "Criar Compromisso")
                                .addOption(
                                        OptionType.STRING,"hora","HH:mm",true
                                ).addOption(OptionType.STRING,"titulo","Título do compromisso",true),
                        Commands.slash("listaragenda", "Lista de compromissos de hoje"),
                        Commands.slash("apagaragenda", "Apaga um compromisso")
                                .addOption(OptionType.INTEGER, "id", "ID do compromisso", true)
                ).queue();
    }

    public void enviarMensagem(Long userId, String mensagem) {

        jda.retrieveUserById(userId)
                .queue(user -> {

                    user.openPrivateChannel()
                            .queue(canal ->
                                    canal.sendMessage(mensagem).queue()
                            );

                }, erro -> {
                    System.out.println("Erro ao localizar usuário: " + erro.getMessage());
                });
    }
    
}
