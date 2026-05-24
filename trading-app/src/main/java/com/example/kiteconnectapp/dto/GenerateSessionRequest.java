package com.example.kiteconnectapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to generate session (exchange request token for access token).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateSessionRequest {
    private String requestToken;
}
