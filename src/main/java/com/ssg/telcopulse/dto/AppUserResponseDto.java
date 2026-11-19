package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.UserRole;

public record AppUserResponseDto(Long id,String userName,UserRole role) {

}
