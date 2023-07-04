package africa.breej.africa.breej.service.auth;


import africa.breej.africa.breej.model.user.User;
import africa.breej.africa.breej.payload.auth.AuthResponse;
import africa.breej.africa.breej.payload.auth.LoginRequest;
import africa.breej.africa.breej.payload.auth.SignUpRequest;

import java.net.URI;
import java.util.Optional;

public interface AuthService {
    AuthResponse authenticateUser(LoginRequest loginRequest);

    URI registerUser(SignUpRequest signUpRequest);

    Optional<User> findByEmail(String email);

    AuthResponse logoutUser(String id);
}
