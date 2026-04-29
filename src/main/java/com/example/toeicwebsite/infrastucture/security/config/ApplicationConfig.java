package com.example.toeicwebsite.infrastucture.security.config;

import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaUserRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return (email)-> new SecurityUser(userMapper.toDomain(jpaUserRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Email not found"))));
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
