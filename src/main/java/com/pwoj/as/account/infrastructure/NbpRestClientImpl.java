package com.pwoj.as.account.infrastructure;

import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.infrastructure.dto.ExchangeData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
class NbpRestClientImpl implements NbpRestClient {

    private final WebClient webClient;

    @Override
    public ExchangeData getExchangeRate(CurrencyCode currencyCode) {
        return webClient.get().uri("exchangerates/rates/a/" + currencyCode)
                .retrieve()
                .bodyToMono(ExchangeData.class)
                .block();
    }
}
