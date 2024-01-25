package ru.asherbakov.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.asherbakov.models.Status;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {
}
