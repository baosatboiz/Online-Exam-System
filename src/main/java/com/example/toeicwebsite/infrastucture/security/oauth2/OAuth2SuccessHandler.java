package com.example.toeicwebsite.infrastucture.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.frontend-url}")
    private String frontendRedirect;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long cookieMaxAge;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
        String token = principal.getJwtToken();
        String cookieDomain = extractDomain(frontendRedirect);
        boolean isSecure = frontendRedirect != null && frontendRedirect.startsWith("https://");

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(isSecure)
                .sameSite(isSecure ? "None" : "Lax")
                .path("/")
                .maxAge(cookieMaxAge);
                
        if (cookieDomain != null && !cookieDomain.equals("localhost") && !cookieDomain.equals("127.0.0.1")) {
            cookieBuilder.domain(cookieDomain);
        }

        ResponseCookie cookie = cookieBuilder.build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        getRedirectStrategy().sendRedirect(request, response, frontendRedirect);
    }

    private String extractDomain(String url) {
        if (url == null) return null;
        try {
            String host = java.net.URI.create(url).getHost();
            if (host != null) {
                return host.startsWith("www.") ? host.substring(4) : host;
            }
        } catch (Exception e) {
            // fallback
        }
        return url.replaceFirst("https?://(www\\.)?", "").split(":")[0];
    }
}
