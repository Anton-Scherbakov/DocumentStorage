package ru.asherbakov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.asherbakov.models.AppealPurpose;
import ru.asherbakov.repository.AppealPurposeRepository;
import ru.asherbakov.service.AppealPurposeService;

import java.util.Optional;

@Service
public class AppealPurposeServiceImpl implements AppealPurposeService {
    @Autowired
    private AppealPurposeRepository appealPurposeRepository;
    @Override
    public Optional<AppealPurpose> getAppealPurpose(Long id) {
        return appealPurposeRepository.findById(id);
    }
    @Override
    public Iterable<AppealPurpose> getAppealPurpose() {
        return appealPurposeRepository.findAll();
    }
}
