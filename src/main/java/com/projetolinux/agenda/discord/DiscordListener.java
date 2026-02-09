package com.projetolinux.agenda.discord;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.projetolinux.agenda.model.Agenda;
import com.projetolinux.agenda.repository.AgendaRepository;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class DiscordListener extends ListenerAdapter {

    private final AgendaRepository agendaRepository;

    public DiscordListener(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String mensagem = event.getMessage().getContentRaw();

        if (mensagem.startsWith("!agenda")) {
            processarInsertAgenda(event, mensagem);
        }

        if(mensagem.startsWith("!apagarAgenda")){
            processarDeleteAgenda(event,mensagem);
        }

        if (mensagem.startsWith("!listarAgenda")) {
            processarListarAgenda(event, mensagem);
        }


    }

  

    private void processarListarAgenda(MessageReceivedEvent event, String mensagem) {
        try {
            LocalDate hoje = LocalDate.now();

            List<Agenda> agendas = agendaRepository.findByDataOrderByHora(hoje);

            if (agendas.isEmpty()) {
                event.getChannel().sendMessage("📭 Nenhum compromisso para hoje.")
                .queue();
                return;
            }

            StringBuilder resposta = new StringBuilder();

            resposta.append("📅 **Agenda de hoje (")
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

            // for (Agenda agenda : agendas) {

            //     Long id =  agenda.getId();
            //     LocalDate data = agenda.getData();
            //     String titulo = agenda.getTitulo();
            //     LocalTime hora = agenda.getHora();

            //     event.getChannel().sendMessage("Evento de id" + id +
            //         "\n Data = " + data +
            //         "\n Titulo = " + titulo +
            //         "\n hora = " + hora 
            //     ).queue();;
            // }

        } catch (Exception e) {
            event.getChannel().sendMessage("❌ Erro ao listar agenda! ").queue();;
        }
    }

private void processarDeleteAgenda(MessageReceivedEvent event, String mensagem) {
    try {
        String[] partes = mensagem.split(" ");

        Long id = Long.parseLong(partes[1]); 

        agendaRepository.deleteById(id);

        event.getChannel().sendMessage(
            "🗑️ Compromisso de id " + id + " deletado com sucesso"
        ).queue();

        if (!agendaRepository.existsById(id)) {
            event.getChannel().sendMessage(
            "⚠️ Não existe compromisso com id " + id
            ).queue();
            return;
}


    } catch (Exception e) {
        event.getChannel().sendMessage(
            "❌ Use: `!apagarAgenda ID`"
        ).queue();
    }
}





    private void processarInsertAgenda(MessageReceivedEvent event, String mensagem){
        try {
            String[] partes = mensagem.split(" ", 4);
            LocalDate data = LocalDate.parse(partes[1]);
            LocalTime hora = LocalTime.parse(partes[2]);
            String titulo = partes[3];

            Agenda agenda = new Agenda();

            agenda.setData(data);
            agenda.setHora(hora);
            agenda.setTitulo(titulo);

            agendaRepository.save(agenda);

            

            event.getChannel().sendMessage(
            "✅ Compromisso salvo!\n📌 " + titulo +
            "\n🗓️ " + data +
            "\n🕒 " + hora
        ).queue();

        } catch (Exception e) {

            event.getChannel().sendMessage(
            "❌ Formato inválido.\n" +
            "Use: `!agenda AAAA-MM-DD HH:mm Título`"
        ).queue();
    }



    
    
}

}
