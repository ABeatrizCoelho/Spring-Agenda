package com.projetolinux.agenda.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projetolinux.agenda.model.MetaEstudo;
import com.projetolinux.agenda.repository.MetaEstudoRepository;

@Service
public class MetaEstudoService {

    private final MetaEstudoRepository metaEstudoRepository;

    public MetaEstudoService(MetaEstudoRepository metaEstudoRepository) {
        this.metaEstudoRepository = metaEstudoRepository;
    }

    public MetaEstudo criarMeta(Long discordUserId,
                                String materia,
                                Integer minutosPorDia) {


        MetaEstudo meta = new MetaEstudo();

        meta.setDiscordUserId(discordUserId);
        meta.setMateria(materia);
        meta.setMinutosPorDia(minutosPorDia);
        meta.setDataInicio(LocalDate.now());
        meta.setAtiva(true);

        return metaEstudoRepository.save(meta);
    }

    public List<MetaEstudo> listarMetas(Long discordUserId) {
        return metaEstudoRepository.findByDiscordUserIdAndAtivaTrue(discordUserId);
    }

    public boolean apagarMeta(Long discordUserId, String materia) {

        MetaEstudo meta = metaEstudoRepository
                .findByDiscordUserIdAndMateria(discordUserId, materia)
                .orElse(null);

        if (meta == null) {
            return false;
        }

        metaEstudoRepository.delete(meta);
        return true;
    }
}