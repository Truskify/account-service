package com.pwoj.as.account.domain;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Builder
@Getter
@Setter
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String surname;
    private String pesel;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SubAccount> subAccounts = Lists.newArrayList();

}
