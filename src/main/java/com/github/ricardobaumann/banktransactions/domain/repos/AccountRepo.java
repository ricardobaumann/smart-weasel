package com.github.ricardobaumann.banktransactions.domain.repos;

import com.github.ricardobaumann.banktransactions.domain.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends CrudRepository<Account, String> {
    Optional<Account> findByIban(String iban);
}
