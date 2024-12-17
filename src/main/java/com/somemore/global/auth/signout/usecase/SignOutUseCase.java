package com.somemore.global.auth.signout.usecase;

import jakarta.servlet.http.HttpServletResponse;

public interface SignOutUseCase {

    void signOut(HttpServletResponse response, String volunteerId);
}
