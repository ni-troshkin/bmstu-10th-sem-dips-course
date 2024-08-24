package com.ratingservice.jwt;

import com.ratingservice.entity.Authorities;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public class JwtAuthentication implements Authentication {
    private boolean authenticated;
    private String username;

    private String firstName;
    private long id;

    private Authorities role;
    private String token;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getRoles();
    }

    public Object getCredentials() { return null; }

    public Object getDetails() { return null; }

    public Object getPrincipal() { return username; }

    public boolean isAuthenticated() { return authenticated; }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }


    public String getUsername() {
        return username;
    }
    public void setName(String name) { firstName = name; }
    public void setUsername(String username) {
        this.username = username;
    }

    public Authorities getRole() {
        return role;
    }

    public void setRole(Authorities role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getName() { return firstName; }


}
