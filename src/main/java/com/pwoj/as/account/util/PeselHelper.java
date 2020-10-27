package com.pwoj.as.account.util;

import java.time.LocalDate;

public class PeselHelper {

    public static LocalDate getBirthDate(String pesel) {
        return LocalDate.of(getBirthYear(pesel), getBirthMonth(pesel), getBirthDay(pesel));
    }

    private static int getBirthYear(String pesel) {
        int year;
        int month;
        year = 10 * Character.getNumericValue(pesel.charAt(0));
        year += Character.getNumericValue(pesel.charAt(1));
        month = 10 * Character.getNumericValue(pesel.charAt(2));
        month += Character.getNumericValue(pesel.charAt(3));
        if (month > 80 && month < 93) {
            year += 1800;
        } else if (month > 0 && month < 13) {
            year += 1900;
        } else if (month > 20 && month < 33) {
            year += 2000;
        } else if (month > 40 && month < 53) {
            year += 2100;
        } else if (month > 60 && month < 73) {
            year += 2200;
        }
        return year;
    }

    private static int getBirthMonth(String pesel) {
        int month;
        month = 10 * Character.getNumericValue(pesel.charAt(2));
        month += Character.getNumericValue(pesel.charAt(3));
        if (month > 80 && month < 93) {
            month -= 80;
        } else if (month > 20 && month < 33) {
            month -= 20;
        } else if (month > 40 && month < 53) {
            month -= 40;
        } else if (month > 60 && month < 73) {
            month -= 60;
        }
        return month;
    }

    private static int getBirthDay(String pesel) {
        int day;
        day = 10 * Character.getNumericValue(pesel.charAt(4));
        day += Character.getNumericValue(pesel.charAt(5));
        return day;
    }
}