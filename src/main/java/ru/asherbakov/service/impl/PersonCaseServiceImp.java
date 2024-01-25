package ru.asherbakov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.asherbakov.enums.Action;
import ru.asherbakov.exceptions.NotFoundException;
import ru.asherbakov.models.*;
import ru.asherbakov.repository.*;
import ru.asherbakov.service.ConvertDataService;
import ru.asherbakov.service.PersonCaseService;
import ru.asherbakov.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PersonCaseServiceImp implements PersonCaseService {
    @Autowired
    private PersonCaseRepository personCaseRepository;
    @Autowired
    private CorrectPersonDataRepository correctPersonDataRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ConvertDataService convertDataService;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserService userService;
    @Value("${data.pathToRootFolder}")
    private String pathToRootFolder;
    // Код статуса "Действующее"
    final int STATUS_CODE_ACTIVE = 1;

    @Override
    public Optional<PersonCase> getPersonCase(String id) {
        return personCaseRepository.findById(id);
    }

    @Override
    public String createPersonCase(String personId, String lastName, String firstName, String middleName, String socialNumber, String birthday) {
        User user = userService.getUser();
        // Приводим ФИО к виду: Фамилия Имя Отчество
        lastName = convertDataService.changeTheLetterCase(lastName);
        firstName = convertDataService.changeTheLetterCase(firstName);
        middleName = convertDataService.changeTheLetterCase(middleName);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfBirthday = LocalDate.parse(birthday, dtf);
        LocalDate createDate = LocalDate.parse(LocalDate.now().format(dtf));
        Status status = statusRepository.findById((long) STATUS_CODE_ACTIVE).orElseThrow();

        PersonCase personCase = new PersonCase();
        // Если дело уже существует (проверка по СНИЛС) - вытягиваем данные
        if (socialNumber != null && !socialNumber.isBlank()) {
            Optional<PersonCase> pc = personCaseRepository.findBySocialNumber(socialNumber);
            if (pc.isPresent()) {
                personCase = pc.get();
            }
        }

        if (personCase.getId() != null && !personCase.getId().isBlank()) {  // Если редактируем дело, либо, пытаемся добавить дело с существующим СНИЛС
            if (personId != null && !personId.isBlank()) {  // Если редактируем дело - вытягиваем его по ID, а не СНИЛС (на всякий случай)
                personCase = getPersonCase(personId).orElseThrow(() -> new NotFoundException("Person case id: " + personId));
                // Обновляем данные в деле
                // P.S.: по т.з., если дело с повторяющимся СНИЛС - то просто открываем его без сохранения внесенных данных
                personCase.setLastName(lastName);
                personCase.setFirstName(firstName);
                personCase.setMiddleName(middleName);
                personCase.setSocialNumber(socialNumber);
                personCase.setDateOfBirthday(dateOfBirthday);
            }

        } else { // Иначе, создаём новое дело
            personCase = new PersonCase(createDate, firstName, lastName, middleName, socialNumber, dateOfBirthday, status);
        }

        personCaseRepository.save(personCase);

        // Создаём на сервере папку для хранения дела пользователя
        String guid = personCase.getId();

        Path pathPersonFolder = Paths.get(pathToRootFolder + "\\" + guid);
        if (!Files.exists(pathPersonFolder)) {
            Log log;
            try {
                Files.createDirectories(pathPersonFolder);
                String message = "Успешно: " + pathToRootFolder + "\\" + guid;
                log = new Log(LocalDateTime.now(), user.getUsername(), user.getRole().getName(), user.getFullName(), guid, socialNumber, null, null, Action.CREATE_PERSON_CASE, message);
                logRepository.save(log);
            } catch (IOException e) {
                String message = "Ошибка: " + e.getMessage();
                log = new Log(LocalDateTime.now(), user.getUsername(), user.getRole().getName(), user.getFullName(), guid, socialNumber, null, null, Action.CREATE_PERSON_CASE_ERROR, message);
                logRepository.save(log);
            }
        }
        return guid;
    }

    @Override
    public String checkingPersonByDatabase(String socialNumber) {
        Optional<CorrectPersonData> correctPersonData = correctPersonDataRepository.findById(convertDataService.convertSocialNumberToNormalView(socialNumber));
        String strJson = "";
        if (correctPersonData.isPresent()) {
            // Формируем Json-строку для отправки frontend`у
            strJson = String.format("{\"socialNumber\":\"%s\",\"lastName\":\"%s\",\"firstName\":\"%s\",\"middleName\":\"%s\",\"dateOfBirthday\":\"%s\"}",
                    correctPersonData.get().getSocialNumber(),
                    correctPersonData.get().getLastName(),
                    correctPersonData.get().getFirstName(),
                    correctPersonData.get().getMiddleName(),
                    correctPersonData.get().getDateOfBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return strJson;
    }
}
