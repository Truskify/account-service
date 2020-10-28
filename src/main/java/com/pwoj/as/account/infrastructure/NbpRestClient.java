package com.pwoj.as.account.infrastructure;

import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.infrastructure.dto.ExchangeData;

public interface NbpRestClient {

    ExchangeData getExchangeRate(CurrencyCode currencyCode);
}
