package com.ratingservice.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RealmRole {
    String[] roles;

    public Authorities toAuthorities() {
        List<Role> rolesLst = new ArrayList<>();
        for (String role : roles) {
            rolesLst.add(new Role(role));
        }
        return new Authorities(rolesLst);
    }
}
