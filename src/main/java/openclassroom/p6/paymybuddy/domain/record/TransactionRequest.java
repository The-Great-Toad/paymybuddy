package openclassroom.p6.paymybuddy.domain.record;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
        @NotNull(message = "Please select a contact") String receiver,
        @Nullable  String description,
//        @Max(value = 255, message = "Description limit of 255 characters reached")
        @NotNull @Min(value = 1, message = "Invalid amount") String amount) {}
