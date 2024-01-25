package ru.asherbakov.service.impl;

import org.springframework.stereotype.Service;
import ru.asherbakov.exceptions.FormatException;
import ru.asherbakov.exceptions.SocialNumberFormatException;
import ru.asherbakov.service.ConvertDataService;

@Service
public class ConvertDataServiceImpl implements ConvertDataService {
    @Override
    public String convertSocialNumberToNormalView(String str) {
        str = str.replaceAll("\\D", "");
        if (str.length() > 11) throw new SocialNumberFormatException("Social number is too long: " + str);
        if (str.length() < 11) {
            int offset = 11 - str.length();
            StringBuilder strBuilder = new StringBuilder(str);
            while (offset > 0) {
                strBuilder.insert(0, "0");
                offset--;
            }
            str = strBuilder.toString();
        }
        char[] array = str.toCharArray();
        int ind = 0;
        StringBuilder result = new StringBuilder();
        for (char ch : array) {
            switch(ind){
                case 3 -> result.append("-").append(ch);
                case 6 -> result.append("-").append(ch);
                case 9 -> result.append(" ").append(ch);
                default -> result.append(ch);
            }
            ind++;
        }
        return result.toString();
    }
    @Override
    public String changeTheLetterCase(String str) {
        boolean doubleNameFlag = str.contains("-");
        return doubleNameFlag ? changeTheLetterCase(str, true) : changeTheLetterCase(str, false);
    }
    @Override
    public String changeTheLetterCase(String str, boolean doubleNameFlag) {
        str = str.replaceAll("[^А-Яа-яA-Za-z-]", "");
        if (!str.isBlank()) {
            if (doubleNameFlag) {
                int separatorIndex = str.indexOf("-");
                if (separatorIndex == 0 || separatorIndex == str.length()-1) throw new FormatException("Illegal format exception: " + str);
                String str1 = str.substring(0, 1).toUpperCase() + str.substring(1, separatorIndex).toLowerCase();
                String str2 = str.substring(separatorIndex+1, separatorIndex+2).toUpperCase() + str.substring(separatorIndex+2).toLowerCase();
                return str1 + '-' + str2;
            } else {
                return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
            }
        }
        return null;
    }
}
