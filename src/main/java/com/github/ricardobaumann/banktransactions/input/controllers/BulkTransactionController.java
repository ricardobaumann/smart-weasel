package com.github.ricardobaumann.banktransactions.input.controllers;

import com.github.ricardobaumann.banktransactions.domain.commands.BulkTransactionCommand;
import com.github.ricardobaumann.banktransactions.domain.usecases.BulkTransferUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/transactions")
public class BulkTransactionController {

    private BulkTransferUseCase bulkTransferUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@RequestBody @Valid BulkTransactionCommand bulkTransactionCommand) {
        bulkTransferUseCase.create(bulkTransactionCommand);
    }

}
