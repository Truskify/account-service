package com.pwoj.as.account.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfiguration {

    @Bean
    public AccountFacade accountFacade(AccountRepository accountRepository) {

        return new AccountFacade(accountRepository, new AccountMapper(), new AccountValidator(accountRepository));
    }
}
