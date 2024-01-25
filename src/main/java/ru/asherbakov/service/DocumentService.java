package ru.asherbakov.service;

import org.springframework.web.multipart.MultipartFile;
import ru.asherbakov.models.Document;
import ru.asherbakov.models.PersonCase;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DocumentService {
    Optional<PersonCase> getPersonCase(String id);

    Optional<Document> getDocumentByteArray(String id);

    Set<Document> getDocumentSet(String id);

    Map<Long, Set<Document>> getDocumentMap(String id);

    void createDocument(String id, MultipartFile[] files, String documentType, String appealPurpose) throws IOException;

    byte[] getDocumentByteArray(Document document) throws IOException;

    File getDocumentFile(Document document) throws IOException;

    String removeDocument(String id);

    String getSavedFilename(Document document);

    String getFilenameForDownload(Document document);
}
