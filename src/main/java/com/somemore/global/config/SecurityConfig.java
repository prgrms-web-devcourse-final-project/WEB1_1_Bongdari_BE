package com.somemore.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.auth.cookie.CookieUseCase;
import com.somemore.global.auth.idpw.filter.IdPwAuthFilter;
import com.somemore.global.auth.jwt.filter.JwtAuthFilter;
import com.somemore.global.auth.jwt.filter.JwtExceptionFilter;
import com.somemore.global.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import com.somemore.global.auth.oauth.handler.CustomOAuthFailureHandler;
import com.somemore.global.auth.oauth.handler.CustomOAuthSuccessHandler;
import com.somemore.global.auth.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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
        idPwAuthFilter.setFilterProcessesUrl("/api/sign-in/id-pw");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/**").permitAll()
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
