package openclassroom.p6.paymybuddy.domain.record;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import openclassroom.p6.paymybuddy.constante.Messages;

public record TransactionRequest(
        @NotNull(message = Messages.RECEIVER_NOT_NULL)
        String receiver,
        @Nullable
        String description,
        @NotNull(message = Messages.AMOUNT_NOT_NULL)
        @Min(value = 1, message = Messages.ACCOUNT_MIN_DEPOSIT)
        String amount) {}