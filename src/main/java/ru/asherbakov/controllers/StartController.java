package ru.asherbakov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.asherbakov.models.PersonCase;
import ru.asherbakov.repository.PersonCaseRepository;

@Controller
public class StartController {
    @Autowired
    private PersonCaseRepository personCaseRepository;

    @GetMapping("/")
    public String home(Model model) {
        Iterable<PersonCase> personCaseIterable = personCaseRepository.findAll();
        model.addAttribute("personCase", personCaseIterable);
        return "main";
    }
    @GetMapping("/help/")
    public String help() {
        return "help";
    }
}
