package ru.asherbakov.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.asherbakov.models.DocumentType;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends CrudRepository<DocumentType, Long> {
    Optional<DocumentType> findDocumentTypeById(Long id);
}
