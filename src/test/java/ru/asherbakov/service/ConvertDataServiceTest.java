package ru.asherbakov.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.asherbakov.exceptions.FormatException;
import ru.asherbakov.exceptions.SocialNumberFormatException;
import ru.asherbakov.service.impl.ConvertDataServiceImpl;

public class ConvertDataServiceTest {
    ConvertDataService convertDataService;

    @BeforeEach
    void setUp() {
        this.convertDataService = new ConvertDataServiceImpl();
    }

    @Test
    public void ifTheSocialNumberIsIncompleteWeMustGetTheFullVersion() {
        int socialNumberTest = 1111111111;
        String result = convertDataService.convertSocialNumberToNormalView(String.valueOf(socialNumberTest));
        Assertions.assertEquals("011-111-111 11", result);
    }

    @Test
    public void whenSocialNumberHasAnIncorrectFormatWeConvertItToNormalForm() {
        String socialNumberTest = "111 -111 111- 11";
        String result = convertDataService.convertSocialNumberToNormalView(socialNumberTest);
        Assertions.assertEquals("111-111-111 11", result);
    }

    @Test
    public void whenSocialNumberIsTooLongThrowSocialNumberFormatException() {
        String socialNumberTest = "111-111-111 111";
        Assertions.assertThrows(SocialNumberFormatException.class, () -> convertDataService.convertSocialNumberToNormalView(socialNumberTest), "Social number format exception");
    }

    @Test
    public void whenSocialNumberIsCorrectReturnTrue() {
        String socialNumberTest = "111-111-111 11";
        String result = convertDataService.convertSocialNumberToNormalView(socialNumberTest);
        Assertions.assertEquals(socialNumberTest, result);
    }

    @Test
    public void whenStringHasAnIncorrectLatterCaseFormatWeConvertItToNormalForm() {
        String stringTest = "щЕрбакоВ";
        String result = convertDataService.changeTheLetterCase(stringTest);
        Assertions.assertEquals("Щербаков", result);
    }

    @Test
    public void whenStringHasAnIncorrectLatterCaseFormatInDoubleNameWeConvertItToNormalForm() {
        String stringTest = "МамИн-polzovatel";
        String result = convertDataService.changeTheLetterCase(stringTest);
        Assertions.assertEquals("Мамин-Polzovatel", result);
    }

    @Test
    public void whenStringHasAnIncorrectLatterInDoubleNameWeConvertItToNormalForm() {
        String stringTest = "МамИ1н-пол.ьзователь_";
        String result = convertDataService.changeTheLetterCase(stringTest);
        Assertions.assertEquals("Мамин-Пользователь", result);
    }
    @Test
    public void whenTheStringEndsInDashThrowFormatException() {
        String stringTest = "Щербаков-";
        Assertions.assertThrows(FormatException.class, () -> convertDataService.changeTheLetterCase(stringTest));
    }
    @Test
    public void whenTheStringStartsWithDashThrowFormatException() {
        String stringTest = "-Щербаков";
        Assertions.assertThrows(FormatException.class, () -> convertDataService.changeTheLetterCase(stringTest));
    }

    @Test
    public void whenStringIsEmptyReturnNull() {
        String stringTest = " ";
        String result = convertDataService.changeTheLetterCase(stringTest);
        Assertions.assertNull(result);
    }
}
