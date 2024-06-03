package openclassroom.p6.paymybuddy.domain.record;

import jakarta.validation.constraints.Min;
import openclassroom.p6.paymybuddy.constante.Messages;

public record AmountRequest(
        @Min(value = 10, message = Messages.ACCOUNT_MIN_DEPOSIT)
        double amount
) {}
