package ru.asherbakov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.asherbakov.models.CorrectPersonData;
import ru.asherbakov.repository.CorrectPersonDataRepository;
import ru.asherbakov.service.FileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private CorrectPersonDataRepository correctPersonDataRepository;
    @Value("${cpd.socialNumber}")
    private String socialNumberField;
    @Value("${cpd.lastName}")
    private String lastNameField;
    @Value("${cpd.firstName}")
    private String firstNameField;
    @Value("${cpd.middleName}")
    private String middleNameField;
    @Value("${cpd.dateOfBirthday}")
    private String dateOfBirthdayField;
    @Value("${cpd.dateFormat}")
    private String dateFormat;
    @Override
    public List<String[]> readCsvFile(MultipartFile multipartFile, String documentSeparator, String documentEncoding) {
        List<String[]> result = new ArrayList<>();
        Charset ch = Charset.forName(documentEncoding);
        try {
            InputStream inputStream = multipartFile.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, ch));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] temp = line.split(documentSeparator);
                result.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void loadPersonDataFile(List<String[]> fileLine){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        Pattern patternSocialNumber = Pattern.compile("\\d{3}-\\d{3}-\\d{3} \\d{2}");
        for (String[] s : fileLine) {
            try {
                if (!patternSocialNumber.matcher(s[Integer.parseInt(socialNumberField)]).find()) {
                    continue;
                }
                CorrectPersonData cpd = new CorrectPersonData(
                        s[Integer.parseInt(socialNumberField)],
                        s[Integer.parseInt(lastNameField)],
                        s[Integer.parseInt(firstNameField)],
                        s[Integer.parseInt(middleNameField)],
                        LocalDate.parse(s[Integer.parseInt(dateOfBirthdayField)], dtf));
                correctPersonDataRepository.save(cpd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
