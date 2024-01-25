package ru.asherbakov.service;

import ru.asherbakov.models.AppealPurpose;

import java.util.Optional;

public interface AppealPurposeService {
    Optional<AppealPurpose> getAppealPurpose(Long id);

    Iterable<AppealPurpose> getAppealPurpose();
}
