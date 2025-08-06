package com.green.greengram.config.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class UserPrincipal implements UserDetails  {
    private final long memberId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(long memberId, List<String> roles) {
        this.memberId = memberId;
        this.authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }

    @Override
    public String getPassword() { return null; }

    @Override
    public String getUsername() { return null; }
}
