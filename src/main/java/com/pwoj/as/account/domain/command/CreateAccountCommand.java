package com.pwoj.as.account.domain.command;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
public class CreateAccountCommand {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    @PESEL
    private String pesel;
    @NotNull
    private BigDecimal accountBalance;
}
