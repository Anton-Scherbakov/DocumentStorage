package ru.asherbakov.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.asherbakov.models.PersonCase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonCaseRepository extends CrudRepository<PersonCase, String> {
    List<PersonCase> findByDocumentSetIsEmpty();
    Optional<PersonCase> findBySocialNumber(String socialNumber);
}
