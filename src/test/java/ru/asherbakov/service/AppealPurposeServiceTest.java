package ru.asherbakov.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.asherbakov.models.AppealPurpose;
import ru.asherbakov.models.Document;
import ru.asherbakov.repository.AppealPurposeRepository;
import ru.asherbakov.service.impl.AppealPurposeServiceImpl;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AppealPurposeServiceTest {
    @Mock
    private AppealPurposeRepository appealPurposeRepository;
    @InjectMocks
    private AppealPurposeService appealPurposeService = new AppealPurposeServiceImpl();
    Set<Document> documentSet = new HashSet<>();

    @Test
    public void whenWeGetAppealPurposeByIdReturnTrueResult() {
        AppealPurpose appealPurpose = new AppealPurpose(1L, "Test", documentSet);
        Mockito.when(appealPurposeRepository.findById(1L)).thenReturn(Optional.of(appealPurpose));
        AppealPurpose result = appealPurposeService.getAppealPurpose(1L).orElse(null);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(appealPurpose, result);
    }

    @Test
    public void whenWeGetAppealPurposeReturnAll() {
        List<AppealPurpose> appealPurposeList = new ArrayList<>();
        appealPurposeList.add(new AppealPurpose(1L, "test1", documentSet));
        appealPurposeList.add(new AppealPurpose(2L, "test2", documentSet));
        appealPurposeList.add(new AppealPurpose(3L, "test3", documentSet));
        Mockito.when(appealPurposeRepository.findAll()).thenReturn(appealPurposeList);
        Iterable<AppealPurpose> result = appealPurposeService.getAppealPurpose();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(appealPurposeList, result);
    }
}
