package com.pwoj.as.account.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccountConfiguration {

    @Bean
    public AccountFacade accountFacade(AccountRepository accountRepository) {
        AccountMapper accountMapper = new AccountMapper();

        return new AccountFacade(new AccountManager(accountRepository, accountMapper), new AccountValidator(accountRepository));
    }
}
