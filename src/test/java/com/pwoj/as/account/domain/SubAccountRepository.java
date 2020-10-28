package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.dto.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface SubAccountRepository extends JpaRepository<SubAccount, UUID> {

    SubAccount findByAccountIdAndCurrency(UUID id, CurrencyCode currencyCode);
}
