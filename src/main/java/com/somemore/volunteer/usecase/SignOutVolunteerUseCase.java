package com.somemore.volunteer.usecase;

import jakarta.servlet.http.HttpServletResponse;

public interface SignOutVolunteerUseCase {

    void signOut(HttpServletResponse response, String volunteerId);
}
