package com.pwoj.as.account.domain;

import com.pwoj.as.account.domain.dto.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
class SubAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Version
    private Long version;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private CurrencyCode currency;
    @Column(nullable = false)
    private BigDecimal balance;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}
