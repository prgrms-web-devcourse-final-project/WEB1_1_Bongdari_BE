package com.somemore.domains.volunteer.service;

import com.somemore.domains.volunteer.usecase.GenerateOAuthUrlUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateOAuthUrlService implements GenerateOAuthUrlUseCase {

    @Value("${app.back-url}")
    private String backendRootUrl;

    @Override
    public String generateUrl(String oAuthProvider) {
        return UriComponentsBuilder.fromHttpUrl(generateBaseUrl())
                .pathSegment(oAuthProvider)
                .build()
                .toUriString();
    }

    private String generateBaseUrl() {
        return UriComponentsBuilder.fromHttpUrl(backendRootUrl)
                .path("/oauth2/authorization")
                .build()
                .toUriString();
    }
}
