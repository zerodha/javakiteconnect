package com.example.kiteconnectapp.controller;

import com.example.kiteconnectapp.dto.GenerateSessionRequest;
import com.example.kiteconnectapp.dto.SessionResponse;
import com.example.kiteconnectapp.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * REST Controller for handling authentication and session management.
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /**
     * GET /api/auth/login-url
     * Returns the Kite login URL for OAuth authentication.
     * User should be redirected to this URL.
     */
    @GetMapping("/login-url")
    public ResponseEntity<?> getLoginURL() {
        log.info("Request for login URL");
        String loginUrl = authenticationService.getLoginURL();
        return ResponseEntity.ok(Collections.singletonMap("loginUrl", loginUrl));
    }
    
    /**
     * POST /api/auth/session
     * Generate session by exchanging request token for access token.
     * Called after user authenticates with Kite.
     * 
     * @param request GenerateSessionRequest containing requestToken
     * @return SessionResponse with access token and user details
     */
    @PostMapping("/session")
    public ResponseEntity<SessionResponse> generateSession(@RequestBody GenerateSessionRequest request) {
        log.info("Generating session");
        SessionResponse response = authenticationService.generateSession(request.getRequestToken());
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/auth/session
     * Get current session details.
     */
    @GetMapping("/session")
    public ResponseEntity<SessionResponse> getSession() {
        log.info("Fetching current session");
        return ResponseEntity.ok(authenticationService.getCurrentSession());
    }
    
    /**
     * POST /api/auth/logout
     * Logout the current user and clear tokens.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("User logout request");
        authenticationService.logout();
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }
}
