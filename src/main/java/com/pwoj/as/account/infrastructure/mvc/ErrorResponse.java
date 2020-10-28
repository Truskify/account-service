package com.pwoj.as.account.infrastructure.mvc;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class ErrorResponse {

    private String errorMessage;
}
