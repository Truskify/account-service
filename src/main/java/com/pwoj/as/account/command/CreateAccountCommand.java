package com.pwoj.as.account.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class CreateAccountCommand {
    private String name;
    private String surname;
    private String pesel;
    private BigDecimal accountBalance;
}
