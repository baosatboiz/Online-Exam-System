package com.example.toeicwebsite.infrastucture.security.oauth2;

import com.example.toeicwebsite.application.command.OAuth2LoginCommand;
import com.example.toeicwebsite.application.result.OAuth2LoginResult;
import com.example.toeicwebsite.application.usecase.OAuth2Login;
import com.example.toeicwebsite.domain.user.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2Login oAuth2Login;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attrs = oAuth2User.getAttributes();

        String provider = request.getClientRegistration().getRegistrationId().toUpperCase();
        String providerId = (String) attrs.get("sub");
        String email = (String) attrs.get("email");
        String picture = (String)attrs.get("picture");

        OAuth2LoginCommand command = new OAuth2LoginCommand(email, picture,providerId, Provider.valueOf(provider));
        OAuth2LoginResult result  = oAuth2Login.execute(command);

        return new OAuth2UserPrincipal(result, attrs);
    }
}
