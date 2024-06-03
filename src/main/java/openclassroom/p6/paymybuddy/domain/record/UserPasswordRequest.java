package openclassroom.p6.paymybuddy.domain.record;

import jakarta.validation.constraints.NotBlank;
import openclassroom.p6.paymybuddy.constante.Messages;

public record UserPasswordRequest(

        @NotBlank(message = Messages.OLD_PWD_NOT_NULL)
        String oldPassword,

        @NotBlank(message = Messages.NEW_PWD_NOT_NULL)
//        @Pattern(regexp = Regex.PASSWORD, message = ErrorMessageValidation.PASSWORD_POLICY)
        String newPassword,

        @NotBlank(message = Messages.CONFIRM_PWD_NOT_NULL)
//        @Pattern(regexp = Regex.PASSWORD, message = ErrorMessageValidation.PASSWORD_POLICY)
        String confirmPassword
) {}
