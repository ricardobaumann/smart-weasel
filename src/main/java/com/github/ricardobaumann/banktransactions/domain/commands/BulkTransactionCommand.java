package com.github.ricardobaumann.banktransactions.domain.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record BulkTransactionCommand(
        @NotBlank @JsonProperty("organization_name") String organizationName,
        @NotBlank @JsonProperty("organization_bic") String organizationBic,
        @NotBlank @JsonProperty("organization_iban") String organizationIban,
        @NotNull @Size(min = 1) @JsonProperty("credit_transfers") List<@Valid CreditTransferCommand> creditTransfers
) {
}
