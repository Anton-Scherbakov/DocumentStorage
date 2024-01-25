package ru.asherbakov.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.asherbakov.models.AppealPurpose;

import java.util.Optional;

@Repository
public interface AppealPurposeRepository extends CrudRepository<AppealPurpose, Long> {
}
