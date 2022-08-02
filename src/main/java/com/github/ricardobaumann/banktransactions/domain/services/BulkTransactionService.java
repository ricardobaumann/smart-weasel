package com.github.ricardobaumann.banktransactions.domain.services;

import com.github.ricardobaumann.banktransactions.domain.commands.BulkTransactionCommand;
import com.github.ricardobaumann.banktransactions.domain.commands.CreditTransferCommand;
import com.github.ricardobaumann.banktransactions.domain.entities.Account;
import com.github.ricardobaumann.banktransactions.domain.entities.Transaction;
import com.github.ricardobaumann.banktransactions.domain.exceptions.BankAccountNotFoundException;
import com.github.ricardobaumann.banktransactions.domain.exceptions.NotEnoughBalanceException;
import com.github.ricardobaumann.banktransactions.domain.repos.AccountRepo;
import com.github.ricardobaumann.banktransactions.domain.repos.TransactionRepo;
import com.github.ricardobaumann.banktransactions.domain.usecases.BulkTransferUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BulkTransactionService implements BulkTransferUseCase {

    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    @Override
    @Transactional
    public void create(BulkTransactionCommand bulkTransactionCommand) {
        Double bulkAmount = bulkTransactionCommand.creditTransfers()
                .stream()
                .map(CreditTransferCommand::amount)
                .mapToDouble(BigDecimal::doubleValue).sum();

        String iban = bulkTransactionCommand.organizationIban();
        accountRepo.findByIban(iban)
                .ifPresentOrElse(
                        account -> {
                            updateAccount(account, bulkAmount);
                            saveTransactions(bulkTransactionCommand.creditTransfers(), account);
                        },
                        () -> {
                            throw new BankAccountNotFoundException(iban);
                        });
    }

    private void saveTransactions(List<CreditTransferCommand> creditTransfers,
                                  Account account) {
        transactionRepo.saveAll(
                creditTransfers
                        .stream().map(ctc -> toTransaction(ctc, account))
                        .collect(Collectors.toList())
        );
    }

    private Transaction toTransaction(CreditTransferCommand ctc, Account account) {
        log.info("Persisting transaction: {}", ctc);
        return Transaction.builder()
                .id(UUID.randomUUID().toString())
                .account(account)
                .amount(ctc.amount())
                .bic(ctc.counterpartyBic())
                .currency(ctc.currency().getCurrencyCode())
                .description(ctc.description())
                .iban(ctc.counterpartyIban())
                .name(ctc.counterpartyName())
                .build();
    }


    private void updateAccount(Account account, Double bulkAmount) {
        BigDecimal newBalance = account.getBalance().subtract(new BigDecimal(bulkAmount));
        log.info("Updating account {} with amount {}", account.getOrganizationName(), newBalance);
        if (newBalance.doubleValue() < 0) {
            throw new NotEnoughBalanceException(account.getIban());
        }
        account.setBalance(newBalance);
        accountRepo.save(account);
    }
}
