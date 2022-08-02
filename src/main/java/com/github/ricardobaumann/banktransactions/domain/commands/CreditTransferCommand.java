package com.github.ricardobaumann.banktransactions.domain.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Currency;

public record CreditTransferCommand(
        @NotNull @Positive BigDecimal amount,
        @NotNull Currency currency,
        @NotNull @NotBlank @JsonProperty("counterparty_name") String counterpartyName,
        @NotNull @NotBlank @JsonProperty("counterparty_bic") String counterpartyBic,
        @NotNull @NotBlank @JsonProperty("counterparty_iban") String counterpartyIban,
        @NotNull @NotBlank String description
) {
}
