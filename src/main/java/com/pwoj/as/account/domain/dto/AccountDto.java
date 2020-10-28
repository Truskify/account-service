package com.pwoj.as.account.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class AccountDto {
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String pesel;
    @NonNull
    private List<SubAccountDto> subAccounts;
}
