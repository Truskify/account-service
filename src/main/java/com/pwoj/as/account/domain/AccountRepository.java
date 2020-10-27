package com.pwoj.as.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsAccountByPesel(String pesel);
}
