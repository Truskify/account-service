package com.pwoj.as.account.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class AccountDto {
    private String name;
    private String surname;
    private String pesel;
    private BigDecimal accountBalance;
}
