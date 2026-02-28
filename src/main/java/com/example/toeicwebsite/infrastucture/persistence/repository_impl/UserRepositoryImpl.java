package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.repository.UserRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaUserRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;
    private final JpaUserRepository jpaUserRepository;
    @Override
    public User findByEmail(String email) {
        return userMapper.toDomain(jpaUserRepository.findByEmail(email).orElse(null));
    }

    @Override
    public User save(User newUser) {
        return userMapper.toDomain(jpaUserRepository.save(userMapper.toEntity(newUser)));
    }

}
