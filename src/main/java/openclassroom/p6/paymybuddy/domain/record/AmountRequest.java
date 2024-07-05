package openclassroom.p6.paymybuddy.domain.record;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import openclassroom.p6.paymybuddy.constante.Messages;

public record AmountRequest(
        @NotNull(message = Messages.AMOUNT_NOT_NULL)
        @Min(value = 10, message = Messages.ACCOUNT_MIN_DEPOSIT)
        double amount
) {}
