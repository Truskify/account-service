package com.pwoj.as.account.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PeselHelperTest {

    @Test
    void getBirthDateWhenPeselBefore2000() {
        String pesel = "26060666347";

        assertEquals(LocalDate.of(1926, 6, 6), PeselHelper.getBirthDate(pesel));

    }

    @Test
    void getBirthDateWhenPeselAfter2000() {
        String pesel = "19260642789";

        assertEquals(LocalDate.of(2019, 6, 6), PeselHelper.getBirthDate(pesel));

    }
}