package com.pwoj.as.account.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExchangeData {
    List<Rate> rates;
}
