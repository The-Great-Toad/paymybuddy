package openclassroom.p6.paymybuddy.domain.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.constante.Regex;

public record UserInfoRequest(

        @NotBlank(message = Messages.REQUIRED)
        @Pattern(regexp = Regex.NAME, message = Messages.ALPHA_CHAR_ONLY)
        String lastname,

        @NotBlank(message = Messages.REQUIRED)
        @Pattern(regexp = Regex.NAME, message = Messages.ALPHA_CHAR_ONLY)
        String firstname
) {}
