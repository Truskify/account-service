package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.AccountDto;
import com.pwoj.as.account.domain.dto.CurrencyCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class AccountFacade {

    private final AccountManager accountManager;
    private final AccountValidator accountValidator;

    public UUID createAccount(CreateAccountCommand command) {
        accountValidator.validateCreateAccountCommand(command);
        log.info("Create new account for pesel [{}].", command.getPesel());

        return accountManager.createAccount(command);
    }

    public AccountDto getAccountDetails(UUID id) {
        log.info("Getting acocunt details for id: [{}].", id);
        return accountManager.getAccountDetails(id);
    }

    public void exchangeMoneyBetweenAccounts(UUID accountId, CurrencyCode sourceCurrency, CurrencyCode targetCurrency, BigDecimal amount) {
        log.info("Try to exchange money for account [{}]. Amount: [{}], exchange currency [{}], target currency [{}].", accountId, amount, sourceCurrency, targetCurrency);
        accountManager.exchangeMoneyBetweenAccounts(accountId, sourceCurrency, targetCurrency, amount);
    }
}
