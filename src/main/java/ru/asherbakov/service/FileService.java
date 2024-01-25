package ru.asherbakov.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String[]> readCsvFile(MultipartFile multipartFile, String documentSeparator, String documentEncoding);

    void loadPersonDataFile(List<String[]> fileLine);

}
