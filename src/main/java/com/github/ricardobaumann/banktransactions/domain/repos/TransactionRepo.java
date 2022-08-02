package com.github.ricardobaumann.banktransactions.domain.repos;

import com.github.ricardobaumann.banktransactions.domain.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends CrudRepository<Transaction, String> {
}
