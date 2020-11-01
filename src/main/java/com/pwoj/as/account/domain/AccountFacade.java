package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.AccountDto;
import com.pwoj.as.account.domain.dto.CurrencyCode;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountFacade {
    UUID createAccount(CreateAccountCommand command);

    AccountDto getAccountDetails(UUID id);

    void exchangeMoneyBetweenAccounts(UUID accountId, CurrencyCode sourceCurrency, CurrencyCode targetCurrency, BigDecimal amount);
}
