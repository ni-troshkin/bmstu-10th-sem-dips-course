package com.statsservice.entity;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    String role;

    public Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
