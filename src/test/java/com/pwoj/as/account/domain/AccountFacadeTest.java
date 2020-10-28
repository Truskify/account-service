package com.pwoj.as.account.domain;

import com.google.common.collect.Lists;
import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.AccountDto;
import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.domain.dto.SubAccountDto;
import com.pwoj.as.account.domain.exception.AccountExistsException;
import com.pwoj.as.account.domain.exception.AccountNotFoundException;
import com.pwoj.as.account.domain.exception.InsufficientFundsException;
import com.pwoj.as.account.domain.exception.UserTooYoungException;
import com.pwoj.as.account.infrastructure.nbp.NbpRestClient;
import com.pwoj.as.account.infrastructure.nbp.dto.ExchangeData;
import com.pwoj.as.account.infrastructure.nbp.dto.Rate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class AccountFacadeTest {
    public static final BigDecimal ACCOUNT_BALANCE = new BigDecimal("122.22");
    public static final String NAME = "username";
    public static final String SURNAME = "surname";
    public static final String VALID_PESEL = "98112315725";

    @Autowired
    private AccountFacade accountFacade;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SubAccountRepository subAccountRepository;
    @MockBean
    private NbpRestClient nbpRestClient;

    @Test
    void shouldCreateAccountAndSubAccounts() {
        UUID id = accountFacade.createAccount(buildCreateAccountCommand("98112315725"));

        List<Account> accounts = accountRepository.findAll();
        SubAccount usdSubAccount = subAccountRepository.findByAccountIdAndCurrency(id, CurrencyCode.USD);
        SubAccount plnSubAccount = subAccountRepository.findByAccountIdAndCurrency(id, CurrencyCode.PLN);
        assertThat(accounts, hasSize(1));
        assertEquals(BigDecimal.ZERO, usdSubAccount.getBalance());
        assertEquals(ACCOUNT_BALANCE, plnSubAccount.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenUserIsTooYoung() {
        String tooYoungPesel = "17210213746"; //should be generated by some library, because it would be outdated some day. Not included for task purpose
        CreateAccountCommand command = buildCreateAccountCommand(tooYoungPesel);

        assertThrows(UserTooYoungException.class, () -> accountFacade.createAccount(command));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyHasAccount() {
        accountRepository.save(Account.builder()
                .name(NAME)
                .surname(SURNAME)
                .pesel(VALID_PESEL)
                .build());
        CreateAccountCommand command = buildCreateAccountCommand(VALID_PESEL);

        assertThrows(AccountExistsException.class, () -> accountFacade.createAccount(command));
    }

    @Test
    void shouldReturnAccountDetails() {
        BigDecimal usdBalance = new BigDecimal("12.11");
        BigDecimal plnBalance = new BigDecimal("123.113");
        Account account = prepareDb(usdBalance, plnBalance);

        AccountDto expectedAccount = buildAccountDto(usdBalance, plnBalance);

        AccountDto result = accountFacade.getAccountDetails(account.getId());

        assertEquals(expectedAccount, result);

    }

    @Test
    void shouldExchangeMoney() {
        BigDecimal plnBalance = new BigDecimal("299.99");
        BigDecimal usdBalance = new BigDecimal("0");
        Account account = prepareDb(usdBalance, plnBalance);
        BigDecimal exchangeRate = new BigDecimal("3.5678");
        prepareNbpRestClientMock(exchangeRate);

        BigDecimal exchangeAmount = new BigDecimal("10.10");
        accountFacade.exchangeMoneyBetweenAccounts(account.getId(), CurrencyCode.PLN, CurrencyCode.USD, exchangeAmount);


        SubAccount plnSubAccount = subAccountRepository.findByAccountIdAndCurrency(account.getId(), CurrencyCode.PLN);
        SubAccount usdSubAccount = subAccountRepository.findByAccountIdAndCurrency(account.getId(), CurrencyCode.USD);
        BigDecimal expectedUsdBalance = usdBalance.add(exchangeAmount.divide(exchangeRate, 2, RoundingMode.HALF_EVEN));

        assertEquals(plnBalance.subtract(exchangeAmount), plnSubAccount.getBalance());
        assertEquals(expectedUsdBalance, usdSubAccount.getBalance());

    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        BigDecimal plnBalance = new BigDecimal("0");
        BigDecimal usdBalance = new BigDecimal("0");
        Account account = prepareDb(usdBalance, plnBalance);

        BigDecimal exchangeAmount = new BigDecimal("10");

        assertThrows(InsufficientFundsException.class,
                () -> accountFacade.exchangeMoneyBetweenAccounts(account.getId(), CurrencyCode.PLN, CurrencyCode.USD, exchangeAmount));

    }

    @Test
    void shouldThrowExceptionWhenWrongCurrencyPair() {
        BigDecimal plnBalance = new BigDecimal("200");
        BigDecimal usdBalance = new BigDecimal("2000");
        Account account = prepareDb(usdBalance, plnBalance);
        prepareNbpRestClientMock(new BigDecimal("1.00"));

        BigDecimal exchangeAmount = new BigDecimal("10");

        assertThrows(IllegalStateException.class,
                () -> accountFacade.exchangeMoneyBetweenAccounts(account.getId(), CurrencyCode.USD, CurrencyCode.USD, exchangeAmount));

    }

    private void prepareNbpRestClientMock(BigDecimal exchangeRate) {
        Mockito.when(nbpRestClient.getExchangeRate(any(CurrencyCode.class))).thenReturn(ExchangeData.builder()
                .rates(Lists.newArrayList(Rate.builder()
                        .mid(exchangeRate)
                        .build()))
                .build());
    }

    @Test
    void shouldReturnExceptionWhenAccountNotFound() {
        assertThrows(AccountNotFoundException.class, () -> accountFacade.getAccountDetails(UUID.randomUUID()));

    }

    private AccountDto buildAccountDto(BigDecimal usdBalance, BigDecimal plnBalance) {
        return AccountDto.builder()
                .pesel(VALID_PESEL)
                .surname(SURNAME)
                .name(NAME)
                .subAccounts(Lists.newArrayList(
                        SubAccountDto.builder()
                                .currency(CurrencyCode.USD)
                                .balance(usdBalance)
                                .build(),
                        SubAccountDto.builder()
                                .currency(CurrencyCode.PLN)
                                .balance(plnBalance)
                                .build()))
                .build();
    }

    private Account prepareDb(BigDecimal usdBalance, BigDecimal plnBalance) {
        Account account = accountRepository.save(Account.builder()
                .name(NAME)
                .surname(SURNAME)
                .pesel(VALID_PESEL)
                .build());
        SubAccount plnSub = subAccountRepository.save(SubAccount.builder()
                .balance(plnBalance)
                .currency(CurrencyCode.PLN)
                .account(account)
                .build());
        SubAccount usdSub = subAccountRepository.save(SubAccount.builder()
                .balance(usdBalance)
                .currency(CurrencyCode.USD)
                .account(account)
                .build());
        account.setSubAccounts(Lists.newArrayList(usdSub, plnSub));
        return account;
    }


    private CreateAccountCommand buildCreateAccountCommand(String pesel) {
        return CreateAccountCommand.builder()
                .balance(ACCOUNT_BALANCE)
                .name(NAME)
                .surname(SURNAME)
                .pesel(pesel)
                .build();
    }
}