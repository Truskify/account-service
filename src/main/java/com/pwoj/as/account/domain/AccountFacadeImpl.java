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
public class AccountFacadeImpl implements AccountFacade {

    private final AccountManager accountManager;
    private final AccountValidator accountValidator;

    @Override
    public UUID createAccount(CreateAccountCommand command) {
        accountValidator.validateCreateAccountCommand(command);
        log.info("Create new account for pesel [{}].", command.getPesel());

        return accountManager.createAccount(command);
    }

    @Override
    public AccountDto getAccountDetails(String pesel) {
        log.info("Getting acocunt details for pesel: [{}].", pesel);
        return accountManager.getAccountDetails(pesel);
    }

    @Override
    public void exchangeMoneyBetweenAccounts(String pesel, CurrencyCode sourceCurrency, CurrencyCode targetCurrency, BigDecimal amount) {
        log.info("Try to exchange money for pesel [{}]. Amount: [{}], exchange currency [{}], target currency [{}].", pesel, amount, sourceCurrency, targetCurrency);
        accountManager.exchangeMoneyBetweenAccounts(pesel, sourceCurrency, targetCurrency, amount);
    }
}
