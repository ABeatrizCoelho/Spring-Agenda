package com.projetolinux.agenda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projetolinux.agenda.model.Agenda;
import com.projetolinux.agenda.repository.AgendaRepository;

@Controller
@RequestMapping("/agenda-view")
public class AgendaViewController {
    @Autowired
    private AgendaRepository agendaRepository;

    @GetMapping
    public String listar(Model model) {
        List<Agenda> agendas = agendaRepository.findAll();
        model.addAttribute("agendas", agendas);
        return "agenda";
    }

    @PostMapping("/salvar")
    public String salvar (Agenda agenda){
        agendaRepository.save(agenda);
        return "redirect:/agenda-view";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id){
        agendaRepository.deleteById(id);
        return "redirect:/agenda-view";
    }
}
