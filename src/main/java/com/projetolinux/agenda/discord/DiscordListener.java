package com.projetolinux.agenda.discord;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;


import com.projetolinux.agenda.model.MetaEstudo;
import com.projetolinux.agenda.model.RegistroEstudo;
import com.projetolinux.agenda.service.MetaEstudoService;
import com.projetolinux.agenda.service.RegistroEstudoService;
import org.springframework.stereotype.Component;

import com.projetolinux.agenda.model.Agenda;
import com.projetolinux.agenda.service.AgendaService;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class DiscordListener extends ListenerAdapter {

    private final AgendaService agendaService;
    private final MetaEstudoService metaEstudoService;
    private final RegistroEstudoService  registroEstudoService;

    public DiscordListener(AgendaService agendaService, MetaEstudoService metaEstudoService, RegistroEstudoService registroEstudoService) {
        this.agendaService = agendaService;
        this.metaEstudoService = metaEstudoService;
        this.registroEstudoService = registroEstudoService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        String mensagem = event.getMessage().getContentRaw();

        if (mensagem.startsWith("!agenda")) {
            processarInsertAgenda(event, mensagem);
        }

        if (mensagem.startsWith("!apagarAgenda")) {
            processarDeleteAgenda(event, mensagem);
        }

        if (mensagem.startsWith("!listarAgenda")) {
            processarListarAgenda(event, mensagem);
        }

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "agenda":
                criarAgenda(event);
                break;

            case "listaragenda":
                listarAgenda(event);
                break;

            case "apagaragenda":
                apagarAgenda(event);
                break;

            case "estudar":
                criarMeta(event);
                break;

            case "estudei":
                registrarEstudo(event);
                break;

            case "progresso":
                mostrarProgresso(event);
                break;

            case "historico":
                mostrarHistorico(event);
                break;

            case "metas":
                listarMetas(event);
                break;

            case "apagarmeta":
                apagarMeta(event);
                break;

            case "help":
                mostrarAjuda(event);
                break;
        }
    }

    private void mostrarAjuda(SlashCommandInteractionEvent event) {

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Ajuda: \n");
        mensagem.append("Agenda: \n");
        mensagem.append("/agenda\n");
        mensagem.append("/listaragenda\n");
        mensagem.append("/apagaragenda\n");
        mensagem.append("Estudos: \n");
        mensagem.append("/estudar\n");
        mensagem.append("/historico\n");
        mensagem.append("/metas\n");
        mensagem.append("/apagarmeta\n");
        mensagem.append("/progresso\n");

        event.getChannel().sendMessage(mensagem.toString()).queue();

    }

    private void apagarMeta(SlashCommandInteractionEvent event) {

        Long discordUserId = event.getUser().getIdLong();

        String materia =
                event.getOption("materia").getAsString();

        boolean apagou =
                metaEstudoService.apagarMeta(
                        discordUserId,
                        materia);

        if (!apagou) {

            event.reply("❌ Meta não encontrada.")
                    .setEphemeral(true)
                    .queue();

            return;
        }

        event.reply("🗑️ Meta de **" + materia + "** removida.")
                .queue();
    }

    private void listarMetas(SlashCommandInteractionEvent event) {

        Long discordUserId =
                event.getUser().getIdLong();

        List<MetaEstudo> metas = metaEstudoService.listarMetas(discordUserId);

        if (metas.isEmpty()) {
            event.reply("📭 Nenhuma meta ativa encontrada.")
                    .queue();
            return;
        }

        StringBuilder resposta = new StringBuilder();

        for (MetaEstudo metaEstudo : metas) {
            resposta.append("Matéria: ")
                    .append(metaEstudo.getMateria())
                    .append("\n")
                    .append("Minutos por dia: ")
                    .append(metaEstudo.getMinutosPorDia());
        }
        event.reply(resposta.toString())
                .queue();
    }


    private void listarAgenda(
            SlashCommandInteractionEvent event) {

        Long discordUserId =
                event.getUser().getIdLong();

        LocalDate hoje = LocalDate.now(
                ZoneId.of("America/Sao_Paulo")
        );

        List<Agenda> agendas =
                agendaService.listarHoje(discordUserId, hoje);

        if (agendas.isEmpty()) {

            event.reply(
                            "📭 Nenhum compromisso para hoje.")
                    .queue();

            return;
        }

        StringBuilder resposta =
                new StringBuilder();

        for (Agenda agenda : agendas) {

            resposta.append("🆔 ")
                    .append(agenda.getId())
                    .append(" | 🕒 ")
                    .append(agenda.getHora())
                    .append("\n📌 ")
                    .append(agenda.getTitulo())
                    .append("\n\n");
        }

        event.reply(resposta.toString())
                .queue();
    }

    private void mostrarHistorico(SlashCommandInteractionEvent event) {

        Long discordUserId = event.getUser().getIdLong();

        List<RegistroEstudo> registros =
                registroEstudoService.gerarHistorico(discordUserId);

        if (registros.isEmpty()) {
            event.reply("📚 Nenhum estudo registrado.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        StringBuilder resposta = new StringBuilder();

        resposta.append("📖 **Histórico de Estudos**\n\n");

        for (RegistroEstudo r : registros) {

            resposta.append("📅 ")
                    .append(r.getData())
                    .append(" | 📚 ")
                    .append(r.getMateria())
                    .append(" | ⏱️ ")
                    .append(r.getMinutos())
                    .append(" min\n");
        }

        event.reply(resposta.toString()).queue();
    }

    private void mostrarProgresso(SlashCommandInteractionEvent event) {

        Long discordUserId = event.getUser().getIdLong();

        StringBuilder resposta = new StringBuilder();

        List<RegistroEstudo> registroEstudos = registroEstudoService.gerarResumoHoje(discordUserId)

        String mensagem =
                registroEstudoService.gerarResumoHoje(discordUserId);

        event.reply(mensagem).queue();
    }

    private void registrarEstudo(SlashCommandInteractionEvent event) {

        try {

            Long discordUserId = event.getUser().getIdLong();

            String materia = event.getOption("materia").getAsString();

            Integer minutos = event.getOption("minutos").getAsInt();

            registroEstudoService.registrarEstudo(
                    discordUserId,
                    materia,
                    minutos);

            event.reply("""
                ✅ Estudo registrado!

                📚 %s
                ⏱️ %d minutos
                """.formatted(materia, minutos))
                    .queue();

        } catch (Exception e) {

            event.reply("❌ Erro ao registrar estudo.")
                    .setEphemeral(true)
                    .queue();
        }
    }

    private void criarMeta(SlashCommandInteractionEvent event) {

        try {

            Long discordUserId = event.getUser().getIdLong();

            String materia = event.getOption("materia").getAsString();

            Integer minutos = event.getOption("minutos").getAsInt();

            metaEstudoService.criarMeta(
                    discordUserId,
                    materia,
                    minutos);

            event.reply("""
                ✅ Meta criada!

                📚 Matéria: %s
                🎯 Meta: %d minutos por dia
                """.formatted(materia, minutos))
                    .queue();

        } catch (Exception e) {
            event.reply("❌ Erro ao criar meta.")
                    .setEphemeral(true)
                    .queue();
        }
    }

    private void processarListarAgenda(MessageReceivedEvent event, String mensagem) {
        try {
            Long discordId = event.getAuthor().getIdLong();
            LocalDate hoje = LocalDate.now();
            List<Agenda> agendas = agendaService.listarHoje(discordId, hoje);

            if (agendas.isEmpty()) {
                event.getChannel().sendMessage("📭 Nenhum compromisso para hoje.")
                        .queue();
                return;
            }

            StringBuilder resposta = new StringBuilder();

            resposta.append("📅 **Agenda de hoje de ")
                    .append(event.getAuthor().getAsMention())
                    .append("(")
                    .append(hoje)
                    .append(")**\n\n");

            for (Agenda agenda : agendas) {
                resposta.append("🆔 ")
                        .append(agenda.getId())
                        .append(" | 🕒 ")
                        .append(agenda.getHora())
                        .append("\n")
                        .append("📌 ")
                        .append(agenda.getTitulo())
                        .append("\n\n");
            }

            event.getChannel().sendMessage(resposta.toString()).queue();

        } catch (Exception e) {
            event.getChannel().sendMessage("❌ Erro ao listar agenda! ").queue();
            ;
        }
    }

    private void processarDeleteAgenda(MessageReceivedEvent event, String mensagem) {
        try {
            Long discordUserId = event.getAuthor().getIdLong();
            String[] partes = mensagem.split(" ");

            Long id = Long.parseLong(partes[1]);

            boolean apagou = agendaService.apagarAgenda(discordUserId, id);

            if (!apagou) {
                event.getChannel()
                        .sendMessage("⚠️ Não existe compromisso com id " + id)
                        .queue();
                return;
            }

            event.getChannel()
                    .sendMessage("🗑️ Compromisso de id " + id + " deletado com sucesso")
                    .queue();

        } catch (Exception e) {
            event.getChannel().sendMessage(
                    "❌ Use: `!apagarAgenda ID`").queue();
        }
    }

    private void processarInsertAgenda(MessageReceivedEvent event, String mensagem) {
        try {
            String[] partes = mensagem.split(" ", 4);
            LocalDate data = LocalDate.parse(partes[1]);
            LocalTime hora = LocalTime.parse(partes[2]);
            String titulo = partes[3];

            Long discordUserId = event.getAuthor().getIdLong();

            agendaService.criarAgenda(
                    discordUserId,
                    data,
                    hora,
                    titulo);

            event.getChannel().sendMessage(
                    "✅ Compromisso salvo! " + event.getAuthor().getAsMention() + "\n📌" + titulo +
                            "\n🗓️ " + data +
                            "\n🕒 " + hora)
                    .queue();

        } catch (Exception e) {

            event.getChannel().sendMessage(
                    "❌ Formato inválido.\n" +
                            "Use: `!agenda AAAA-MM-DD HH:mm Título`")
                    .queue();
        }

    }


    private void apagarAgenda(
            SlashCommandInteractionEvent event) {

        Long discordUserId =
                event.getUser().getIdLong();

        Long id =
                event.getOption("id").getAsLong();

        boolean apagou =
                agendaService.apagarAgenda(
                        discordUserId,
                        id);

        if (!apagou) {

            event.reply(
                            "⚠️ Compromisso não encontrado.")
                    .setEphemeral(true)
                    .queue();

            return;
        }

        event.reply(
                        "🗑️ Compromisso removido.")
                .queue();
    }

    private void criarAgenda(
            SlashCommandInteractionEvent event) {

        try {

            LocalDate data = LocalDate.parse(
                    event.getOption("data").getAsString());

            LocalTime hora = LocalTime.parse(
                    event.getOption("hora").getAsString());

            String titulo =
                    event.getOption("titulo").getAsString();

            Long discordUserId =
                    event.getUser().getIdLong();

            agendaService.criarAgenda(
                    discordUserId,
                    data,
                    hora,
                    titulo);

            event.reply(
                            "✅ Compromisso salvo!\n" +
                                    "📌 " + titulo +
                                    "\n🗓️ " + data +
                                    "\n🕒 " + hora)
                    .queue();

        } catch (Exception e) {

            event.reply(
                            "Erro ao criar compromisso.")
                    .setEphemeral(true)
                    .queue();
        }
    }

}
