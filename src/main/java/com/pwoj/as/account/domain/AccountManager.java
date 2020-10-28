package com.pwoj.as.account.domain;

import com.google.common.collect.Lists;
import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
class AccountManager{

    private final AccountRepository accountRepository;

    UUID createAccount(CreateAccountCommand command) {

        SubAccount plnAccount = SubAccount.builder()
                .currency(CurrencyCode.PLN)
                .balance(command.getBalance())
                .build();
        SubAccount usdAccount = SubAccount.builder()
                .currency(CurrencyCode.PLN)
                .balance(BigDecimal.ZERO)
                .build();

        return accountRepository.save(Account.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .pesel(command.getPesel())
                .subAccounts(Lists.newArrayList(plnAccount, usdAccount))
                .build())
                .getId();

    }
}
