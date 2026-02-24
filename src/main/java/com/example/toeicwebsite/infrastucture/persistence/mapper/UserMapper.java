package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.user.model.Role;
import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.infrastucture.persistence.entity.UserEntity;
import com.example.toeicwebsite.infrastucture.security.config.SecurityUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "bussinessId",target = "userId")
    @Mapping(source = "role",target="userRole")
    User toDomain(UserEntity user);
    default UserId map(UUID id) {
        return new UserId(id);
    }
    default Role map(String role){
        try{
            return Role.valueOf(role);
        }
        catch(Exception e){
            System.err.println("DEBUG: Không thể map Role từ chuỗi: [" + role + "]");
            return null;
        }
    }
}
