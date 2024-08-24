package com.libraryservice.jwt;

import com.libraryservice.entity.RealmRole;
import io.jsonwebtoken.Claims;

public class JwtUtils {
    public static JwtAuthentication generate(Claims claims, String token) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
//        System.out.println(claims.toString());
//        System.out.println(claims.get("id"));
//        System.out.println(Long.valueOf(claims.get("id").toString()));
//        jwtInfoToken.setRole(getRole(claims));
//        jwtInfoToken.setId(Long.valueOf(claims.get("id").toString()));
        jwtInfoToken.setUsername(claims.get("preferred_username").toString());
        jwtInfoToken.setToken(token);
        jwtInfoToken.setRole(claims.get("realm_access", RealmRole.class).toAuthorities());
        return jwtInfoToken;
    }

//    private static Role getRole(Claims claims) {
//        final Role role = Role.valueOf(claims.get("roles", String.class));
//        return role;
//    }
}
