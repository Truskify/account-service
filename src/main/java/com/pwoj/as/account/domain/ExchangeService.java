package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.domain.exception.InsufficientFundsException;
import com.pwoj.as.account.infrastructure.nbp.NbpRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
class ExchangeService {
    private final SubAccountRepository subAccountRepository;
    private final NbpRestClient nbpRestClient;

    void exchangeMoney(UUID accountId, CurrencyCode sourceCurrency, CurrencyCode targetCurrency, BigDecimal amount) {
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
}
