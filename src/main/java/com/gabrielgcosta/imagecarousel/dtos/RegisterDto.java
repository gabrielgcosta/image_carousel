package com.gabrielgcosta.imagecarousel.dtos;

import com.gabrielgcosta.imagecarousel.domain.user.UserRole;

public record RegisterDto(String login, String password, UserRole role) {
    
}
