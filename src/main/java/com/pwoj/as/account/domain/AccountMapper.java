package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.dto.AccountDto;
import com.pwoj.as.account.domain.dto.SubAccountDto;

import java.util.List;
import java.util.stream.Collectors;

class AccountMapper {

    AccountDto mapToAccountDto(Account account) {
        return AccountDto.builder()
                .pesel(account.getPesel())
                .name(account.getName())
                .surname(account.getSurname())
                .subAccounts(mapToSubAccountDtos(account.getSubAccounts()))
                .build();
    }

    private List<SubAccountDto> mapToSubAccountDtos(List<SubAccount> subAccounts) {
        return subAccounts.stream()
                .map(this::mapToSubAccountDto)
                .collect(Collectors.toList());

    }

    private SubAccountDto mapToSubAccountDto(SubAccount subAccount) {
        return SubAccountDto.builder()
                .balance(subAccount.getBalance())
                .currency(subAccount.getCurrency())
                .build();
    }
}
