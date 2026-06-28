package com.projetolinux.agenda.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projetolinux.agenda.model.MetaEstudo;
import com.projetolinux.agenda.model.RegistroEstudo;
import com.projetolinux.agenda.repository.MetaEstudoRepository;
import com.projetolinux.agenda.repository.RegistroEstudoRepository;

@Service
public class RegistroEstudoService {

    private final RegistroEstudoRepository registroEstudoRepository;
    private final MetaEstudoRepository metaEstudoRepository;

    public RegistroEstudoService(
            RegistroEstudoRepository registroEstudoRepository,
            MetaEstudoRepository metaEstudoRepository) {

        this.registroEstudoRepository = registroEstudoRepository;
        this.metaEstudoRepository = metaEstudoRepository;
    }

    public RegistroEstudo registrarEstudo(
            Long discordUserId,
            String materia,
            Integer minutos) {

        RegistroEstudo registro = new RegistroEstudo();

        registro.setDiscordUserId(discordUserId);
        registro.setMateria(materia);
        registro.setMinutos(minutos);
        registro.setData(LocalDate.now());

        return registroEstudoRepository.save(registro);
    }

    public List<RegistroEstudo> gerarHistorico(Long discordUserId) {
        return registroEstudoRepository
                .findByDiscordUserIdOrderByDataDesc(discordUserId);
    }

}