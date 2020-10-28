package com.pwoj.as.account.infrastructure.nbp;

import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.infrastructure.nbp.dto.ExchangeData;

public interface NbpRestClient {

    ExchangeData getExchangeRate(CurrencyCode currencyCode);
}
