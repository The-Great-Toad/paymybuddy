package openclassroom.p6.paymybuddy.domain.record;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRequest(
        @NotBlank(message = "Please enter an email")
        @Email(message = "Please enter a valid email")
        String email) {}
