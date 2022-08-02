package com.github.ricardobaumann.banktransactions.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
        reason = "Account does not have enough balance")
public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(String iban) {
        super(iban);
    }

    public NotEnoughBalanceException() {

    }
}
