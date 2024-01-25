package ru.asherbakov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.asherbakov.models.DocumentType;
import ru.asherbakov.repository.DocumentTypeRepository;
import ru.asherbakov.service.DocumentTypeService;

import java.util.Optional;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Override
    public Optional<DocumentType> getDocumentType(Long id) {
        return documentTypeRepository.findDocumentTypeById(id);
    }
    @Override
    public Iterable<DocumentType> getDocumentType() {
        return documentTypeRepository.findAll();
    }
}
