package com.pwoj.as.account.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@EqualsAndHashCode
public class SubAccountDto {
    private CurrencyCode currency;
    private BigDecimal balance;
}
