package com.somemore.global.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.auth.cookie.CookieUseCase;
import com.somemore.auth.idpw.filter.IdPwAuthFilter;
import com.somemore.auth.jwt.filter.JwtAuthFilter;
import com.somemore.auth.jwt.filter.JwtExceptionFilter;
import com.somemore.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import com.somemore.auth.oauth.handler.failure.CustomOAuthFailureHandler;
import com.somemore.auth.oauth.handler.success.CustomOAuthSuccessHandler;
import com.somemore.auth.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;
    private final CustomOAuthFailureHandler customOAuthFailureHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   AuthenticationManager authenticationManager,
                                                   GenerateTokensOnLoginUseCase generateTokensOnLoginUseCase,
                                                   CookieUseCase cookieUseCase,
                                                   ObjectMapper objectMapper) throws Exception {

        IdPwAuthFilter idPwAuthFilter = new IdPwAuthFilter(authenticationManager, generateTokensOnLoginUseCase, cookieUseCase, objectMapper);
        idPwAuthFilter.setFilterProcessesUrl("/api/center/sign-in");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(request ->
                                request
                                        .requestMatchers(
                                                "/api/center/sign-in",
                                                "/**"
//                                        "/login",
//                                        "/oauth2/**",
//                                        "/api/auth/**",
//                                        "/v3/api-docs/**",
//                                        "/swagger/**",
//                                        "/swagger-ui.html",
//                                        "/swagger-ui/**"
                                        )
                                        .permitAll()
                                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 ->
                        oauth2
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(customOAuth2UserService))
                                .failureHandler(customOAuthFailureHandler)
                                .successHandler(customOAuthSuccessHandler)
                );


        return httpSecurity
                .addFilterBefore(idPwAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
