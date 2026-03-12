package com.example.toeicwebsite.web.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMyProfileResponse{
    private String email;
    private String businessId;
    private List<String> roles;
    private String picture;
}
