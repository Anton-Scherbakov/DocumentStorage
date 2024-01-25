package ru.asherbakov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.asherbakov.exceptions.NotFoundException;
import ru.asherbakov.models.*;
import ru.asherbakov.service.*;
import ru.asherbakov.service.impl.ZipPackageServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Controller
public class DocumentController {
    @Autowired
    private PersonCaseService personCaseService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserService userService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private AppealPurposeService appealPurposeService;
    @Value("${data.pathToRootFolder}")
    private String pathToRootFolder;

    @GetMapping("/document/{id}")
    private String documentShow(@PathVariable(name = "id") String caseId,
                                Model model) {
        Optional<PersonCase> personCase = documentService.getPersonCase(caseId);
        if (personCase.isPresent()) {
            model.addAttribute("personCase", personCase.get());
            model.addAttribute("documentType", documentTypeService.getDocumentType());
            model.addAttribute("appealPurpose", appealPurposeService.getAppealPurpose());
            model.addAttribute("documentSet", documentService.getDocumentSet(caseId));
            model.addAttribute("documentMap", documentService.getDocumentMap(caseId));
        } else {
            throw new NotFoundException("Person case not found: " + caseId);
        }
        return "document";
    }

    //TODO: BindingResult для модальной формы... как перенаправлять ответ
    @PostMapping("/document/{id}")
    public String documentAdd(@PathVariable(name = "id") String caseId,
                               @RequestParam(name = "uploadFile") MultipartFile[] files,
                               @RequestParam(name = "documentType") String documentType,
                               @RequestParam(name = "documentPurpose") String appealPurpose
//                               @Valid
//                               BindingResult bindingResult,
    ) throws IOException {
        documentService.createDocument(caseId, files, documentType, appealPurpose);
        return "redirect:/document/{id}";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable("id") String id) throws IOException {
        Document document = documentService.getDocumentByteArray(id).orElseThrow();
        final String resultFilename = documentService.getFilenameForDownload(document);

        //        Определяем HTTP заголовки
        ContentDisposition contentDisposition = ContentDisposition.inline()
                .filename(resultFilename, StandardCharsets.UTF_8)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));

        //        Скачиваем файл
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(documentService.getDocumentByteArray(document));
    }

    @GetMapping("/download/all/{id}")
    public ResponseEntity<byte[]> downloadsAll(@PathVariable("id") String caseId,
                                                Authentication authentication) throws IOException {
        Set<Document> documentSet = documentService.getDocumentSet(caseId);
        User user =  userService.getUser();
        Map<Path, String> documentMap = new HashMap<>();
        for (Document document : documentSet) {
            final String filename = documentService.getSavedFilename(document);
            final String resultFilename =
                    LocalDate.now().toString() + "_" +
                            document.getPersonCase().getLastName() + " " + document.getPersonCase().getFirstName() + " (" +
                            document.getDocumentType().getName().toLowerCase() + ")." + StringUtils.getFilenameExtension(document.getOriginalName());
            final String pathToFile = pathToRootFolder + '\\' + caseId + '\\';
            final Path path = Paths.get(pathToFile + filename);
            documentMap.put(path, resultFilename);
        }
        PersonCase personCase = personCaseService.getPersonCase(caseId).orElseThrow(() -> new NotFoundException("Person case id: " + caseId));
        final String filename = LocalDate.now().toString() + "_" + personCase.getFullName();
        ZipPackageServiceImpl zip = new ZipPackageServiceImpl();
        byte[] byteFile = zip.createZipFile(documentMap, filename);


        //        Определяем HTTP заголовки
        ContentDisposition contentDisposition = ContentDisposition.inline()
                .filename(filename + ".zip", StandardCharsets.UTF_8)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));

        //        Скачиваем файл
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(byteFile);
    }

    @GetMapping("/open/{id}")
    public ResponseEntity<InputStreamResource> open(@PathVariable("id") String id) throws IOException {
        Document document = documentService.getDocumentByteArray(id).orElseThrow(NotFoundException::new);
        String filename = documentService.getSavedFilename(document);
        String mimeType = URLConnection.guessContentTypeFromName(filename);
        if (mimeType == null) {
            mimeType = "text/plain";
        }

        File file = documentService.getDocumentFile(document);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "inline;filename=" + filename);
        MediaType mediaType = MediaType.parseMediaType(mimeType);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") String id) throws IOException {
        return "redirect:/document/" + documentService.removeDocument(id);
    }
}
