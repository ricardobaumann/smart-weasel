package com.github.ricardobaumann.banktransactions.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@Table(name = "transactions")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private String id;

    private String name;

    private String iban;

    private String bic;

    private BigDecimal amount;

    private String currency;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String description;
}
