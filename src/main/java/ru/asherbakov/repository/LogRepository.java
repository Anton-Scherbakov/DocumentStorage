package ru.asherbakov.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.asherbakov.models.Log;

@Repository
public interface LogRepository extends CrudRepository<Log, String> {
}
