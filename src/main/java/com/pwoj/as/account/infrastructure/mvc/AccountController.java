package com.pwoj.as.account.infrastructure.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwoj.as.account.domain.AccountFacade;
import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.AccountDto;
import com.pwoj.as.account.domain.dto.CurrencyCode;
import com.pwoj.as.account.domain.exception.AccountExistsException;
import com.pwoj.as.account.domain.exception.AccountNotFoundException;
import com.pwoj.as.account.domain.exception.InsufficientFundsException;
import com.pwoj.as.account.domain.exception.UserTooYoungException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

@RequestMapping("/v1/api/account")
@RestController
@RequiredArgsConstructor
@Slf4j
class AccountController {

    private final AccountFacade accountFacade;
    private final ObjectMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createAccount(@RequestBody @Valid CreateAccountCommand command) {

        return accountFacade.createAccount(command);
    }

    @GetMapping("/{id}")
    public AccountDto getAccountDetails(@PathVariable UUID id) {

        return accountFacade.getAccountDetails(id);
    }

    @GetMapping("/{id}/exchange")
    public void getAccountDetails(@PathVariable UUID id,
                                  @RequestParam CurrencyCode sourceCurrency,
                                  @RequestParam CurrencyCode targetCurrency,
                                  @RequestParam BigDecimal amount) {

        accountFacade.exchangeMoneyBetweenAccounts(id, sourceCurrency, targetCurrency, amount);
    }

    @ExceptionHandler({AccountNotFoundException.class})
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(buildErrorResponseJson(e));
    }

    @ExceptionHandler({UserTooYoungException.class})
    public ResponseEntity<String> handleUserTooYoungException(UserTooYoungException e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(buildErrorResponseJson(e));
    }

    @ExceptionHandler({AccountExistsException.class})
    public ResponseEntity<String> handleAccountExistsException(AccountExistsException e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(buildErrorResponseJson(e));
    }

    @ExceptionHandler({InsufficientFundsException.class})
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(buildErrorResponseJson(e));
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(buildErrorResponseJson(e));
    }


    @ExceptionHandler({WebClientResponseException.class})
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException e) {
        log.warn("Error response code: {}, response body: {}", e.getStatusCode(), e.getResponseBodyAsString());
        return ResponseEntity.status(e.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(e.getResponseBodyAsString());
    }

    private String buildErrorResponseJson(Exception e) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .build());
    }
}
