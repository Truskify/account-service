package com.pwoj.as.account.domain;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String pesel;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SubAccount> subAccounts = Lists.newArrayList();

}
