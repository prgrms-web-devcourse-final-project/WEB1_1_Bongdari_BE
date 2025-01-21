package com.somemore.support.annotation;

import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory.class)
public @interface MockUser {

    String userId() default "123e4567-e89b-12d3-a456-426614174000";

    String roleId() default "123e4567-e89b-12d3-a456-426614174000";

    String role() default "ROLE_VOLUNTEER";
}
