package com.example.toeicwebsite.infrastucture.security.config;

import com.example.toeicwebsite.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Data
@NoArgsConstructor
public class SecurityUser implements UserDetails {
    private User user;
    private String picture;
    public SecurityUser(User user) {
        this.user = user;
    }

    public SecurityUser(User user, String picture) {
        this.user = user;
        this.picture = picture;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getUserRole().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name())).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
