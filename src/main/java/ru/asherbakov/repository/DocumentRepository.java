package ru.asherbakov.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.asherbakov.models.Document;

import java.util.Optional;
import java.util.Set;

@Repository
public interface DocumentRepository extends CrudRepository<Document, String> {
    Set<Document> findDocumentByPersonCaseIdAndEnabledTrue(String id);

    Optional<Document> findDocumentById(String id);
}
