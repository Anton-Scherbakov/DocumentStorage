package ru.asherbakov.service;

public interface ConvertDataService {
    String convertSocialNumberToNormalView(String str);

    String changeTheLetterCase(String str);

    String changeTheLetterCase(String str, boolean doubleNameFlag);
}
