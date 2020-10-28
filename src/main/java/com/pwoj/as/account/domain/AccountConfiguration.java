package com.pwoj.as.account.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccountConfiguration {

    @Bean
    public AccountFacade accountFacade(AccountRepository accountRepository) {

        return new AccountFacade(new AccountManager(accountRepository), new AccountValidator(accountRepository));
    }
}
