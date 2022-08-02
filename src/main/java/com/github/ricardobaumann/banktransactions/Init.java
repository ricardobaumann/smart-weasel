package com.github.ricardobaumann.banktransactions;

import com.github.ricardobaumann.banktransactions.domain.entities.Account;
import com.github.ricardobaumann.banktransactions.domain.repos.AccountRepo;
import com.github.ricardobaumann.banktransactions.domain.repos.TransactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@AllArgsConstructor
public class Init implements CommandLineRunner {

    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    @Override
    public void run(String... args) {
        transactionRepo.deleteAll();
        accountRepo.deleteAll();
        accountRepo.save(
                Account.builder()
                        .id(UUID.randomUUID().toString())
                        .bic("OIVUSCLQXXX")
                        .iban("FR10474608000002006107XXXXX")
                        .organizationName("ACME Corp")
                        .balance(new BigDecimal(100_000))
                        .build()
        );
    }
}
