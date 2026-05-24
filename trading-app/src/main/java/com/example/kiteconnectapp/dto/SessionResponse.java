package com.example.kiteconnectapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response containing session/authentication details after successful login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {
    private String accessToken;
    private String publicToken;
    private String userId;
    private String userName;
    private String email;
    private String brokerName;
    private boolean authenticated;
    private String message;
}
