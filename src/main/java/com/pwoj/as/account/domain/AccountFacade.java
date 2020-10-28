package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.command.CreateAccountCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class AccountFacade {

    private final AccountManager accountManager;
    private final AccountValidator accountValidator;

    public UUID createAccount(CreateAccountCommand command) {
        accountValidator.validateCreateAccountCommand(command);
        log.info("Create new account for pesel [{}].", command.getPesel());

        return accountManager.createAccount(command);
    }
}
