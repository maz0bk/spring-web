package com.vgur.spring.auth.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generateToken(UserDetails userDetails);
}
