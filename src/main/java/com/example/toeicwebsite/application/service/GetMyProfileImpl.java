package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.query.GetMyProfileQuery;
import com.example.toeicwebsite.application.result.GetMyProfileResult;
import com.example.toeicwebsite.application.usecase.GetMyProfile;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyProfileImpl implements GetMyProfile {
    private final UserRepository userRepository;
    @Override
    public GetMyProfileResult execute(GetMyProfileQuery query) {
        User me = userRepository.findByEmail(query.email());
        if(me==null) throw new DomainNotFoundException("Email's not found or deleted");
        return new GetMyProfileResult(me.getEmail(),me.getUserId(),me.getUserRole());
    }
}
