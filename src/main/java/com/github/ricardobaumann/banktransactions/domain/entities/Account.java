package com.github.ricardobaumann.banktransactions.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@Table(name = "accounts")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String id;

    @Column(name = "organization_name")
    private String organizationName;

    private BigDecimal balance;

    private String iban;

    private String bic;

}
