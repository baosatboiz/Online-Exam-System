package com.example.toeicwebsite.infrastucture.security.oauth2;

import com.example.toeicwebsite.application.result.OAuth2LoginResult;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class OAuth2UserPrincipal implements OAuth2User {

    private final OAuth2LoginResult loginResult;
    private final Map<String, Object> attributes;

    public OAuth2UserPrincipal(OAuth2LoginResult loginResult, Map<String, Object> attributes) {
        this.loginResult = loginResult;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return loginResult.roles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
    }

    @Override
    public String getName() { return loginResult.email(); }

    public String getJwtToken() { return loginResult.token(); }
}
