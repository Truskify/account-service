package com.pwoj.as.account.domain;

import com.google.common.collect.Lists;
import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.CurrencyCode;

import java.math.BigDecimal;

class AccountMapper {

    Account mapToEntity(CreateAccountCommand command) {

        SubAccount plnAccount = SubAccount.builder()
                .currency(CurrencyCode.PLN)
                .balance(command.getBalance())
                .build();
        SubAccount usdAccount = SubAccount.builder()
                .currency(CurrencyCode.PLN)
                .balance(BigDecimal.ZERO)
                .build();

        return Account.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .pesel(command.getPesel())
                .subAccounts(Lists.newArrayList(plnAccount, usdAccount))
                .build();

    }


}
