package com.fuples.auth.contoller;

import com.fuples.auth.dto.AuthRequest;
import com.fuples.auth.dto.AuthResponse;
import com.fuples.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse.Register> register(@Valid @RequestBody AuthRequest.Register request) {
        AuthResponse.Register response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse.Token> login(@Valid @RequestBody AuthRequest.Login request) {
        AuthResponse.Token response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse.Token> refresh(@Valid @RequestBody AuthRequest.Refresh request) {
        AuthResponse.Token response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody(required = false) AuthRequest.Logout request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        String token = null;
        if (request != null) {
            token = request.refreshToken();
        }
        if ((token == null || token.isBlank()) && authorizationHeader != null) {
            token = authorizationHeader;
        }
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }
}
