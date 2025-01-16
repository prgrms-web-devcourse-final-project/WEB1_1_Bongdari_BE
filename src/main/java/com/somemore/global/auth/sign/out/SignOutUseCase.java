package com.somemore.global.auth.sign.out;

import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface SignOutUseCase {

    void signOut(HttpServletResponse response, UUID userId);
}
