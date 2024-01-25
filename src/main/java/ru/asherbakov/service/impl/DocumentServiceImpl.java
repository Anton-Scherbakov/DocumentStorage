package ru.asherbakov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.asherbakov.enums.Action;
import ru.asherbakov.exceptions.NotFoundException;
import ru.asherbakov.models.*;
import ru.asherbakov.repository.*;
import ru.asherbakov.service.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private PersonCaseRepository personCaseRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AppealPurposeRepository appealPurposeRepository;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private PersonCaseService personCaseService;
    @Autowired
    private AppealPurposeService appealPurposeService;
    @Value("${data.pathToRootFolder}")
    private String pathToRootFolder;

    @Override
    public Optional<PersonCase> getPersonCase(String id) {
        return personCaseRepository.findById(id);
    }
    @Override
    public Optional<Document> getDocumentByteArray(String id) {
        return documentRepository.findById(id);
    }
    @Override
    public Set<Document> getDocumentSet(String id) {
        return documentRepository.findDocumentByPersonCaseIdAndEnabledTrue(id);
    }

    @Override
    public Map<Long, Set<Document>> getDocumentMap(String id) {
        Set<Document> documentSet = getDocumentSet(id);
        Map<Long, Set<Document>> documentMap = new HashMap<>();
        for (Document d : documentSet) {
            Set<Document> tempSet = new HashSet<>();
            if (documentMap.containsKey(d.getAppealPurpose().getId())) {
                tempSet = documentMap.get(d.getAppealPurpose().getId());
            }
            tempSet.add(d);
            documentMap.put(d.getAppealPurpose().getId(), tempSet);
        }
        return documentMap;
    }

    @Override
    public void createDocument(String id, MultipartFile[] files, String documentType, String appealPurpose) throws IOException {
        User user = userService.getUser();
        PersonCase personCase = personCaseService.getPersonCase(id).orElseThrow(NotFoundException::new);
        MultipartFile mf = null;
        File f = null;
        Log log;

        //TODO: Добавить роль и полное имя в конструктор
        String userFullName = userService.getUserFullName(user);
        String userRole = user.getRole().getName();

        String pathToUpload = pathToRootFolder + '\\' + id + '\\';
        Document document = new Document(
                LocalDateTime.now(),
                user.getUsername(),
                documentTypeService.getDocumentType(Long.valueOf(documentType)).orElseThrow(NotFoundException::new),
                personCase,
                appealPurposeService.getAppealPurpose(Long.valueOf(appealPurpose)).orElseThrow(NotFoundException::new),
                true
        );

        String name = documentRepository.save(document).getId();
        if (files.length > 1) {
            ZipPackageServiceImpl zip = new ZipPackageServiceImpl();
            f = zip.createZipFile(files, name);
            mf = new MockMultipartFile(f.getName(), new FileInputStream(f));
            document.setOriginalName(f.getName());
            pathToUpload += f.getName();
            document.setFileName(f.getName());
        } else {
            mf = files[0];
            document.setOriginalName(files[0].getOriginalFilename());
            document.setFileName(name + '.' + StringUtils.getFilenameExtension(files[0].getOriginalFilename()));
            pathToUpload += name + '.' + StringUtils.getFilenameExtension(files[0].getOriginalFilename()); //files[0].getOriginalFilename();
        }

        try {
            Path path = Paths.get(pathToUpload);
            mf.transferTo(path);
            String message = "Успешно: " + mf.getOriginalFilename() + " -> " + pathToUpload + '\\';
            log = new Log(LocalDateTime.now(), user.getUsername(), user.getRole().getName(), user.getFullName(),
                    id, personCase.getSocialNumber(), document.getId(), document.getDocumentType().getName(), Action.CREATE_DOCUMENT, message);
            log.setSocialNumber(personCase.getSocialNumber());
            log.setUserFullName(user.getFullName());
            logRepository.save(log);
        } catch (Exception e) {
            String message = "Ошибка: " + e.getMessage();
            log = new Log(LocalDateTime.now(), user.getUsername(), user.getRole().getName(), user.getFullName(),
                    id, personCase.getSocialNumber(), null, null, Action.CREATE_DOCUMENT_ERROR, message);
            logRepository.save(log);
        }

    }

    @Override
    public byte[] getDocumentByteArray(Document document) throws IOException {
        Path path = Paths.get(pathToFile(document));
        return Files.readAllBytes(path);
    }
    @Override
    public File getDocumentFile(Document document) throws IOException {
        return new File(pathToFile(document));
    }

    @Override
    public String removeDocument(String id) {
        Log log;
        User user = userService.getUser();
        Document document = documentRepository.findDocumentById(id).orElseThrow(NotFoundException::new);
        try {
            String message = String.format("Успешно: удалён документ %s из каталога %s", document.getFileName(), document.getPersonCase());
            String filename = document.getFileName();
            String pathToFile = pathToRootFolder + '\\' + document.getPersonCase().getId() + '\\';
            Path path = Paths.get(pathToFile + filename);
            Files.deleteIfExists(path);
            document.setEnabled(false);
            documentRepository.save(document);
            log = new Log(LocalDateTime.now(), user.getUsername(), user.getRole().getName(), user.getFullName(),
                    document.getPersonCase().getId(), document.getPersonCase().getSocialNumber(), id, document.getDocumentType().getName(), Action.DELETE_DOCUMENT, message);
            logRepository.save(log);
        } catch (Exception e) {
            String message = "Ошибка: " + e.getMessage();
            log = new Log(LocalDateTime.now(), user.getUsername(), user.getRole().getName(), user.getFullName(),
                    document.getPersonCase().getId(), document.getPersonCase().getSocialNumber(), id, document.getDocumentType().getName(), Action.DELETE_DOCUMENT_ERROR, message);
            logRepository.save(log);
        }
        return document.getPersonCase().getId();
    }

    @Override
    public String getSavedFilename(Document document) {
        return document.getId() + '.' + StringUtils.getFilenameExtension(document.getOriginalName());
    }
    @Override
    public String getFilenameForDownload(Document document){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh-mm");
        return LocalDateTime.now().format(dtf) + "_" +
                document.getPersonCase().getFullName() + " (" +
                document.getDocumentType().getName().toLowerCase() + ")." + StringUtils.getFilenameExtension(document.getOriginalName());
    }

    private String pathToFile(Document document) {
        return pathToRootFolder + '\\' + document.getPersonCase().getId() + '\\' + getSavedFilename(document);
    }

}
