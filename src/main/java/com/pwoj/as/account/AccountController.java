package com.pwoj.as.account;

import com.pwoj.as.account.domain.AccountFacade;
import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/v1/rest/account")
@RestController
@RequiredArgsConstructor
class AccountController {

    private final AccountFacade accountFacade;

    @PostMapping
    public UUID createAccount(@RequestBody @Valid CreateAccountCommand command) {

        return accountFacade.createAccount(command);
    }

    @GetMapping("/{id}")
    public AccountDto getAccountDetails(@PathVariable UUID id) {

        return accountFacade.getAccountDetails(id);
    }
}
