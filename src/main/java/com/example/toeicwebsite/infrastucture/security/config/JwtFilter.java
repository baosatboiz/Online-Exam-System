package com.example.toeicwebsite.infrastucture.security.config;

import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaUserRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JpaUserRepository jpaUserRepository;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader==null||!requestHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = requestHeader.substring(7);
        try{
            String userEmail = jwtUtils.parseToken(token).getSubject();
            SecurityUser securityUser = new SecurityUser(userMapper.toDomain(jpaUserRepository.findByEmail(userEmail).orElse(null)));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(securityUser,null,securityUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch(Exception e){

        }
        filterChain.doFilter(request,response);
    }
}
