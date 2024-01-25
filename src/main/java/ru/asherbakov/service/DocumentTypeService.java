package ru.asherbakov.service;

import ru.asherbakov.models.DocumentType;

import java.util.Optional;

public interface DocumentTypeService {
    Optional<DocumentType> getDocumentType(Long id);

    Iterable<DocumentType> getDocumentType();
}
