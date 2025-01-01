package com.somemore.global.auth.oauth.checker;

import com.somemore.global.auth.oauth.domain.OAuthProvider;

public interface OAuthInfoChecker {
    boolean doesUserExist(OAuthProvider provider, String id);
}
