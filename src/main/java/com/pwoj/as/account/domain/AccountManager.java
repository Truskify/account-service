package com.pwoj.as.account.domain;

import com.google.common.collect.Lists;
import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.AccountDto;
import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.domain.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Slf4j
class AccountManager {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ExchangeService exchangeService;

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

    AccountDto getAccountDetails(String pesel) {
        Account account = getAccountByPesel(pesel);

        return accountMapper.mapToAccountDto(account);
    }

    void exchangeMoneyBetweenAccounts(String pesel, CurrencyCode sourceCurrency, CurrencyCode targetCurrency, BigDecimal amount) {
        UUID accountId = getAccountByPesel(pesel)
                .getId();
        exchangeService.exchangeMoney(accountId, sourceCurrency, targetCurrency, amount);
    }

    private AccountNotFoundException createAccountNotExistsException(String pesel) {
        log.error("Account does not exist for given pesel [{}].", pesel);
        return new AccountNotFoundException("Account does not exists.");
    }

    private Account getAccountByPesel(String pesel) {
        return accountRepository.findAccountByPesel(pesel)
                .orElseThrow(() -> createAccountNotExistsException(pesel));
    }
}
