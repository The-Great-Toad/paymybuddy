package openclassroom.p6.paymybuddy.domain.record;

import jakarta.validation.constraints.NotBlank;
import openclassroom.p6.paymybuddy.constante.Messages;

public record UserInfoRequest(

        @NotBlank(message = Messages.REQUIRED)
        String lastname,

        @NotBlank(message = Messages.REQUIRED)
        String firstname
) {}
