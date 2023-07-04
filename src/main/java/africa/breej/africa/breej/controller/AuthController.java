package africa.breej.africa.breej.controller;

import africa.breej.africa.breej.payload.Response;
import africa.breej.africa.breej.payload.auth.AuthResponse;
import africa.breej.africa.breej.payload.auth.LoginRequest;
import africa.breej.africa.breej.payload.auth.SignUpRequest;
import africa.breej.africa.breej.security.CurrentUser;
import africa.breej.africa.breej.security.TokenProvider;
import africa.breej.africa.breej.security.UserPrincipal;
import africa.breej.africa.breej.service.auth.AuthService;
import africa.breej.africa.breej.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    AuthService authService;

    private AuthenticationManager authenticationManager;

    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

        URI location = authService.registerUser(signUpRequest);

        LoginRequest loginRequest = new LoginRequest();
        if(!StringUtil.isBlank(loginRequest.getEmail()))
            loginRequest.setEmail(signUpRequest.getEmail());
        loginRequest.setPhoneNumber(signUpRequest.getPhoneNumber());
        loginRequest.setPassword(signUpRequest.getPassword());
        AuthResponse authResponse = authService.authenticateUser(loginRequest);

        return ResponseEntity.created(location)
                .body(new Response(true, true,"User registered successfully", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CurrentUser UserPrincipal userPrincipal) {
        AuthResponse authResponse = authService.logoutUser(userPrincipal.getId());
        return ResponseEntity.ok(authResponse);
    }
}
