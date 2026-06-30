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

                        // Agenda
                        Commands.slash("agenda", "Criar um compromisso")
                                .addOption(OptionType.STRING, "data", "AAAA-MM-DD", true)
                                .addOption(OptionType.STRING, "hora", "HH:mm", true)
                                .addOption(OptionType.STRING, "titulo", "Título do compromisso", true),

                        Commands.slash("listaragenda", "Lista os compromissos de hoje"),

                        Commands.slash("apagaragenda", "Apaga um compromisso")
                                .addOption(OptionType.INTEGER, "id", "ID do compromisso", true),

                        // estudos
                        Commands.slash("estudar", "Criar uma meta de estudos")
                                .addOption(OptionType.STRING, "materia", "Ex: Java", true)
                                .addOption(OptionType.INTEGER, "minutos", "Minutos por dia", true),

                        Commands.slash("estudei", "Registrar estudo realizado")
                                .addOption(OptionType.STRING, "materia", "Ex: Java", true)
                                .addOption(OptionType.INTEGER, "minutos", "Minutos estudados", true),

                        Commands.slash("resumohoje", "Mostra seu progresso de estudos"),

                        Commands.slash("historico", "Mostra seu histórico de estudos"),

                        Commands.slash("metas", "Lista todas as metas"),

                        Commands.slash("apagarmeta", "Apaga uma meta")
                                .addOption(OptionType.STRING, "materia", "Nome da matéria", true),

                        Commands.slash("ajuda", "Lista todos os comandos")
                )
                .queue();
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
