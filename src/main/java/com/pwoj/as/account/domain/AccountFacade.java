package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.command.CreateAccountCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountFacade {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public UUID createAccount(CreateAccountCommand command) {
        log.info("Create new account for pesel [{}].", command.getPesel());

        return accountRepository.save(accountMapper.mapToEntity(command))
                .getId();
    }
}
