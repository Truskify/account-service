package com.pwoj.as.account.domain;

import com.pwoj.as.account.infrastructure.NbpRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class AccountConfiguration {

    @Value("${nbp.url}")
    private String nbpUrl;

    @Bean
    public AccountFacade accountFacade(AccountRepository accountRepository, SubAccountRepository subAccountRepository, NbpRestClient nbpRestClient) {
        AccountMapper accountMapper = new AccountMapper();

        return new AccountFacade(new AccountManager(accountRepository, subAccountRepository, accountMapper, nbpRestClient), new AccountValidator(accountRepository));
    }

    @Bean
    public WebClient gameHistoryWebClient() {
        return WebClient
                .builder()
                .baseUrl(nbpUrl)
                .build();
    }
}
