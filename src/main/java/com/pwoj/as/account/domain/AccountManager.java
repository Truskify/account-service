package com.pwoj.as.account.domain;

import com.google.common.collect.Lists;
import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.AccountDto;
import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.domain.exception.AccountNotFoundException;
import com.pwoj.as.account.domain.exception.InsufficientFundsException;
import com.pwoj.as.account.infrastructure.NbpRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Slf4j
class AccountManager {

    private final AccountRepository accountRepository;
    private final SubAccountRepository subAccountRepository;
    private final AccountMapper accountMapper;
    private final NbpRestClient nbpRestClient;

    UUID createAccount(CreateAccountCommand command) {

        SubAccount plnSubAccount = SubAccount.builder()
                .currency(CurrencyCode.PLN)
                .balance(command.getBalance())
                .build();
        SubAccount usdSubAccount = SubAccount.builder()
                .currency(CurrencyCode.USD)
                .balance(BigDecimal.ZERO)
                .build();

        Account account = Account.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .pesel(command.getPesel())
                .subAccounts(Lists.newArrayList(plnSubAccount, usdSubAccount))
                .build();

        plnSubAccount.setAccount(account);
        usdSubAccount.setAccount(account);

        return accountRepository.save(account)
                .getId();


    }

    AccountDto getAccountDetails(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> createAccountNotExistsException(id));

        return accountMapper.mapToAccountDto(account);
    }

    void exchangeMoneyBetweenAccounts(UUID accountId, CurrencyCode sourceCurrency, CurrencyCode targetCurrency, BigDecimal amount) {
        CurrencyCode currency = CurrencyCode.PLN.equals(sourceCurrency) ? targetCurrency : sourceCurrency;

        SubAccount sourceCurrencySubAccount = subAccountRepository.findByAccountIdAndCurrency(accountId, sourceCurrency);
        SubAccount targetCurrencySubAccount = subAccountRepository.findByAccountIdAndCurrency(accountId, targetCurrency);
        if (amount.compareTo(sourceCurrencySubAccount.getBalance()) > 0) {
            log.error("Insufficient funds to make exchange for account in [{}].", sourceCurrency);
            throw new InsufficientFundsException("Insufficient funds to make exchange.");
        }
        BigDecimal rate = nbpRestClient.getExchangeRate(currency).getRates().get(0).getMid();

        if (CurrencyCode.PLN.equals(targetCurrency)) {
            BigDecimal targetAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
            targetCurrencySubAccount.setBalance(targetCurrencySubAccount.getBalance().add(targetAmount));
            sourceCurrencySubAccount.setBalance(sourceCurrencySubAccount.getBalance().subtract(amount));
        } else if (CurrencyCode.PLN.equals(sourceCurrency)) {
            BigDecimal targetAmount = amount.divide(rate, 2, RoundingMode.HALF_EVEN);
            targetCurrencySubAccount.setBalance(targetCurrencySubAccount.getBalance().add(targetAmount));
            sourceCurrencySubAccount.setBalance(sourceCurrencySubAccount.getBalance().subtract(amount));
        } else {
            log.error("At least one of currency should be in PLN.");
            throw new IllegalStateException("At least one of currency should be in PLN.");
        }
        subAccountRepository.save(targetCurrencySubAccount);
        subAccountRepository.save(sourceCurrencySubAccount);
    }

    private AccountNotFoundException createAccountNotExistsException(UUID id) {
        log.error("Account does not exist for given id [{}].", id);
        return new AccountNotFoundException("Account does not exists.");
    }
}
