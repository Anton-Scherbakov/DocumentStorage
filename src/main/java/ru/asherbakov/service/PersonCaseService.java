package ru.asherbakov.service;

import ru.asherbakov.models.PersonCase;

import java.util.Optional;

public interface PersonCaseService {
    Optional<PersonCase> getPersonCase(String id);

    String createPersonCase(String personId, String lastName, String firstName, String middleName, String socialNumber, String birthday);

    String checkingPersonByDatabase(String socialNumber);
}
