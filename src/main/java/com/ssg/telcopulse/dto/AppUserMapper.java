package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.AppUser;

public class AppUserMapper {

    public static AppUserResponseDto toDto(AppUser user)
    {
        return new AppUserResponseDto(user.getId(),user.getUserName(),user.getRole());
    }

    public static AppUser toEntity(AppUserRequestDto request)
    {
        return new AppUser(request.userName(),request.password(),request.role());
    }
}
