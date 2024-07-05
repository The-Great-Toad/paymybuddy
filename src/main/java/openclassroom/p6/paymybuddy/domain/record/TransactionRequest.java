package openclassroom.p6.paymybuddy.domain.record;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import openclassroom.p6.paymybuddy.constante.Messages;

public record TransactionRequest(
        @NotNull(message = Messages.RECEIVER_NOT_NULL) String receiver,
        @Nullable  String description,
        @NotNull @Min(value = 1, message = Messages.AMOUNT_NOT_NULL) String amount) {}