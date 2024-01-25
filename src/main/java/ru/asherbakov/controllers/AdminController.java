package ru.asherbakov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.asherbakov.models.CorrectPersonData;
import ru.asherbakov.models.Log;
import ru.asherbakov.models.PersonCase;
import ru.asherbakov.models.User;
import ru.asherbakov.repository.CorrectPersonDataRepository;
import ru.asherbakov.repository.LogRepository;
import ru.asherbakov.repository.PersonCaseRepository;
import ru.asherbakov.repository.UserRepository;
import ru.asherbakov.service.FileService;
import ru.asherbakov.service.UserService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class AdminController {
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private PersonCaseRepository personCaseRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @GetMapping("/admin/")
    public String admin() {
        return "admin/admin";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("uploadFile") MultipartFile file,
                             @RequestParam String documentSeparator,
                             @RequestParam String documentEncoding) {
        fileService.loadPersonDataFile(fileService.readCsvFile(file, documentSeparator, documentEncoding));
        return "redirect:/admin/";
    }

    @GetMapping("/log")
    public String log(Model model) {
        Iterable<Log> log = logRepository.findAll();
        model.addAttribute("log", log);
        return "admin/log";
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        Iterable<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/download/empty-case")
    public ResponseEntity<byte[]> downloadEmptyCase() {
        String filename = LocalDate.now().toString() + " список пустых дел.csv";
        StringBuilder report = new StringBuilder();
        report.append("GUID;Дата создания;СНИЛС;Фамилия;Имя;Отчество;Дата рождения;Статус\n");
        List<PersonCase> emptyPersonCase = personCaseRepository.findByDocumentSetIsEmpty();
        for (PersonCase p : emptyPersonCase) {
            report.append(
                    p.getId() + ";" +
                            p.getCreateDate() + ";" +
                            p.getSocialNumber() + ";" +
                            p.getLastName() + ";" +
                            p.getFirstName() + ";" +
                            p.getMiddleName() + ";" +
                            p.getDateOfBirthday() + ";" +
                            p.getStatus().getName() + ";" + "\n");
        }
        //        Определяем HTTP заголовки
        ContentDisposition contentDisposition = ContentDisposition.inline()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));

        //        Формируем файл
        byte[] byteFile = report.toString().getBytes(Charset.forName("windows-1251"));
        //        Скачиваем файл
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(byteFile);
    }
}
