package openclassroom.p6.paymybuddy.domain.record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.constante.Regex;

public record UserRequest(
        @NotBlank(message = Messages.REQUIRED)
        String lastname,
        @NotBlank(message = Messages.REQUIRED)
        String firstname,
        @NotBlank(message = Messages.REQUIRED)
        @Email(message = Messages.EMAIL_INVALID)
        String email,
        @NotBlank(message = Messages.REQUIRED)
        @Pattern(regexp = Regex.PASSWORD, message = Messages.PASSWORD_POLICY)
        String password

) {
}
