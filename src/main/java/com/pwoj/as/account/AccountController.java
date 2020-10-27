package com.pwoj.as.account;

import com.pwoj.as.account.command.CreateAccountCommand;
import com.pwoj.as.account.dto.AccountDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/rest")
@RestController
class AccountController {

    @PostMapping("/account")
    public AccountDto createAccount(@RequestBody CreateAccountCommand command) {

        //TODO - need to implement logic
        return null;
    }
}
