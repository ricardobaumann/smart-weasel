package com.github.ricardobaumann.banktransactions.domain.services;

import com.github.ricardobaumann.banktransactions.domain.commands.BulkTransactionCommand;
import com.github.ricardobaumann.banktransactions.domain.commands.CreditTransferCommand;
import com.github.ricardobaumann.banktransactions.domain.entities.Account;
import com.github.ricardobaumann.banktransactions.domain.entities.Transaction;
import com.github.ricardobaumann.banktransactions.domain.exceptions.BankAccountNotFoundException;
import com.github.ricardobaumann.banktransactions.domain.exceptions.NotEnoughBalanceException;
import com.github.ricardobaumann.banktransactions.domain.repos.AccountRepo;
import com.github.ricardobaumann.banktransactions.domain.repos.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkTransactionServiceTest {

    @Mock
    private AccountRepo mockAccountRepo;

    @Mock
    private TransactionRepo transactionRepo;

    private BulkTransactionService bulkTransactionServiceUnderTest;

    @Captor
    private ArgumentCaptor<List<Transaction>> transactionArgumentCaptor;

    @BeforeEach
    void setUp() {
        bulkTransactionServiceUnderTest = new BulkTransactionService(mockAccountRepo, transactionRepo);
    }

    @Test
    void testCreate() {
        // Setup
        final BulkTransactionCommand bulkTransactionCommand = new BulkTransactionCommand("organizationName",
                "organizationBic", "iban",
                List.of(new CreditTransferCommand(new BigDecimal("2.00"), Currency.getInstance("USD"),
                        "counterpartyName", "counterpartyBic", "counterpartyIban", "description")));

        // Configure AccountRepo.findByIban(...).
        Account account = new Account("id", "organizationName", new BigDecimal("2.00"), "iban", "bic");
        final Optional<Account> opAccount = Optional.of(
                account);
        when(mockAccountRepo.findByIban("iban")).thenReturn(opAccount);

        // Configure AccountRepo.save(...).
        when(mockAccountRepo.save(account)).thenReturn(account);
        when(transactionRepo.saveAll(any())).thenReturn(null);

        // Run the test
        bulkTransactionServiceUnderTest.create(bulkTransactionCommand);

        // Verify the results
        verify(mockAccountRepo).save(account);
        verify(transactionRepo).saveAll(transactionArgumentCaptor.capture());
        assertThat(transactionArgumentCaptor.getValue())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("name", "counterpartyName")
                .hasFieldOrPropertyWithValue("iban", "counterpartyIban")
                .hasFieldOrPropertyWithValue("bic", "counterpartyBic")
                .hasFieldOrPropertyWithValue("amount", new BigDecimal("2.00"))
                .hasFieldOrPropertyWithValue("currency", "USD")
                .hasFieldOrPropertyWithValue("account", account)
                .hasFieldOrPropertyWithValue("description", "description");
    }

    @Test
    void testCreate_AccountRepoFindByIbanReturnsAbsent() {
        // Setup
        final BulkTransactionCommand bulkTransactionCommand = new BulkTransactionCommand("organizationName",
                "organizationBic", "iban",
                List.of(new CreditTransferCommand(new BigDecimal("0.00"), Currency.getInstance("USD"),
                        "counterpartyName", "counterpartyBic", "counterpartyIban", "description")));
        when(mockAccountRepo.findByIban("iban")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> bulkTransactionServiceUnderTest.create(bulkTransactionCommand))
                .isInstanceOf(BankAccountNotFoundException.class);

        // Verify the results
        verify(mockAccountRepo, never()).save(Mockito.any());
    }

    @Test
    void testCreate_NotEnoughBalance() {
        // Setup
        final BulkTransactionCommand bulkTransactionCommand = new BulkTransactionCommand("organizationName",
                "organizationBic", "iban",
                List.of(new CreditTransferCommand(new BigDecimal("2.00"), Currency.getInstance("USD"),
                        "counterpartyName", "counterpartyBic", "counterpartyIban", "description")));

        // Configure AccountRepo.findByIban(...).
        Account account = new Account("id", "organizationName", new BigDecimal("1.99"), "iban", "bic");
        final Optional<Account> opAccount = Optional.of(
                account);
        when(mockAccountRepo.findByIban("iban")).thenReturn(opAccount);

        // Run the test
        assertThatThrownBy(() -> bulkTransactionServiceUnderTest.create(bulkTransactionCommand))
                .isInstanceOf(NotEnoughBalanceException.class);

        // Verify the results
        verify(mockAccountRepo, never()).save(account);
    }
}
