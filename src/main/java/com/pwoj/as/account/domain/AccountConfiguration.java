package com.pwoj.as.account.domain;

import com.pwoj.as.account.infrastructure.nbp.NbpRestClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
class AccountConfiguration {

    @Value("${nbp.url}")
    private String nbpUrl;

    @Value("${nbp.timeout}")
    private Integer timeout;

    @Bean
    public AccountFacadeImpl accountFacade(AccountRepository accountRepository, SubAccountRepository subAccountRepository, NbpRestClient nbpRestClient) {
        AccountMapper accountMapper = new AccountMapper();

        return new AccountFacadeImpl(new AccountManager(accountRepository, subAccountRepository, accountMapper, nbpRestClient), new AccountValidator(accountRepository));
    }

    @Bean
    public WebClient gameHistoryWebClient() {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                                .doOnConnected(conn -> conn
                                        .addHandlerLast(new ReadTimeoutHandler(timeout))
                                        .addHandlerLast(new WriteTimeoutHandler(timeout))));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
        return WebClient
                .builder()
                .baseUrl(nbpUrl)
                .clientConnector(connector)
                .build();
    }
}
