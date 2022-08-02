package com.github.ricardobaumann.banktransactions.domain.usecases;

import com.github.ricardobaumann.banktransactions.domain.commands.BulkTransactionCommand;

public interface BulkTransferUseCase {
    void create(BulkTransactionCommand bulkTransactionCommand);
}
