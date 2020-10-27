package com.pwoj.as.account.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class SubAccountDto {
    private CurrencyCode currency;
    private BigDecimal balance;
}
