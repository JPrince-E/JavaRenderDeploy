package africa.breej.africa.breej.payload.auth;

import africa.breej.africa.breej.model.user.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private Role role;

    private String referralCode;

    // Getters and Setters (Omitted for brevity)
}
