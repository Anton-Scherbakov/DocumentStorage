package ru.asherbakov.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.asherbakov.models.CorrectPersonData;

@Repository
public interface CorrectPersonDataRepository extends CrudRepository<CorrectPersonData, String> {

}
