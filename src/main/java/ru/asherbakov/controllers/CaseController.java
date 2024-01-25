package ru.asherbakov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.asherbakov.service.FileService;
import ru.asherbakov.service.PersonCaseService;

@Controller
@RequestMapping("/person")
public class CaseController {
    @Autowired
    private PersonCaseService personCaseService;

    /**
     * Добавление в базу нового дела (папки пользователя)
     *
     * @param lastName фамилия
     * @param firstName имя
     * @param middleName отчество
     * @param socialNumber СНИЛС
     * @param birthday дата рождения
     * @return ссылка на заведенное дело
     */
    @PostMapping
    public String personAdd(@RequestParam(defaultValue = "") String personId,
                            @RequestParam String lastName,
                            @RequestParam String firstName,
                            @RequestParam String middleName,
                            @RequestParam String socialNumber,
                            @RequestParam String birthday) {

        return "redirect:/document/" + personCaseService.createPersonCase(
                personId, lastName, firstName, middleName, socialNumber, birthday
        );
    }

    @GetMapping("/check")
    public @ResponseBody String checkData(@RequestParam String socialNumber) {
        return personCaseService.checkingPersonByDatabase(socialNumber);
    }
}
