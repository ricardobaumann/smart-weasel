package com.github.ricardobaumann.banktransactions.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Account does not exist")
public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String iban) {
        super(iban);
    }
}
