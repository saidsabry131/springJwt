package org.example.springjwt.service;

import org.example.springjwt.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UsersPrincipal implements UserDetails {
    Users users;
    public UsersPrincipal(Users user) {
        this.users=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.stream(getRoles(users)).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }


    private String[] getRoles(Users users)
    {
        if (users.getRole()==null)
            return new String[]{"USER"};

        else {
            return users.getRole().split(",");
        }
    }
}
