package openclassroom.p6.paymybuddy.domain.record;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import openclassroom.p6.paymybuddy.constante.Messages;

public record ContactRequest(
        @NotBlank(message = Messages.EMAIL_NOT_NULL)
        @Email(message = Messages.EMAIL_INVALID)
        String email) {}
