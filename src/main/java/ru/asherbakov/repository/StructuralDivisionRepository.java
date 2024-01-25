package ru.asherbakov.repository;

import ru.asherbakov.models.StructuralDivision;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructuralDivisionRepository extends CrudRepository<StructuralDivision, Long> {
}
