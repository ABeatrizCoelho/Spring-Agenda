package com.projetolinux.agenda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetolinux.agenda.model.Agenda;
import com.projetolinux.agenda.repository.AgendaRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaRepository agendaRepository;

    @PostMapping
    public Agenda criar(@RequestBody Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    @GetMapping
    public List<Agenda> listarTodos(){
        return agendaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Agenda buscarPorId(@PathVariable Long id) {
        return agendaRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        agendaRepository.deleteById(id);
    }


    
}
