package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.command.CreateAccountCommand;
import com.pwoj.as.account.domain.exception.AccountExistsException;
import com.pwoj.as.account.domain.exception.UserTooYoungException;
import com.pwoj.as.account.util.PeselHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.pl.PESELValidator;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@RequiredArgsConstructor
class AccountValidator {

    private final AccountRepository accountRepository;

    void validateCreateAccountCommand(CreateAccountCommand command) {
        log.info("Validate create account request.");
        validateMaturity(command);
        validateAccountExistence(command);
    }

    private void validateAccountExistence(CreateAccountCommand command) {
        if (accountRepository.existsAccountByPesel(command.getPesel())) {
            log.error("Account already exists for pesel: [{}].", command.getPesel());
            throw new AccountExistsException("Account already exists.");
        }
    }

    private void validateMaturity(CreateAccountCommand command) {
        LocalDate birthDate = PeselHelper.getBirthDate(command.getPesel());
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
            log.error("User must be at least 18 years old to create account. Date of birth: [{}].", birthDate);
            throw new UserTooYoungException("User is tUser must be at least 18 years old.");
        }
    }
}
