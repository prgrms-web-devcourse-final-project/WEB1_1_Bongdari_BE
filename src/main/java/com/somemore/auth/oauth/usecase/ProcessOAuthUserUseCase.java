package com.somemore.auth.oauth.usecase;

import jakarta.servlet.ServletException;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface ProcessOAuthUserUseCase {
    String processOAuthUser(Authentication authentication) throws IOException, ServletException;
}
