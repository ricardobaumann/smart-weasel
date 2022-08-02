package com.github.ricardobaumann.banktransactions.input.controllers;

import com.github.ricardobaumann.banktransactions.domain.exceptions.NotEnoughBalanceException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class ExceptionAdvice implements ProblemHandling {

    @ExceptionHandler
    public ResponseEntity<Problem> handleDbConstraintViolation(
            final ConstraintViolationException exception,
            final NativeWebRequest request) {

        if (exception.getConstraintName().equals("account_balance_check")) {
            throw new NotEnoughBalanceException();
        } else {
            return handleThrowable(exception, request);
        }
    }

}
