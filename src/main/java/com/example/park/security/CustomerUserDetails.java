package com.example.park.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.park.common.UserRole;

import lombok.Getter;

@Getter
public class CustomerUserDetails implements UserDetails{
    private final Long userId;
    private final String username;
    private final String password;
    private final Integer role;
    private final Collection<? extends GrantedAuthority> authorities;
    public CustomerUserDetails(Long userId,String username,String password,Integer role){
        this.userId=userId;
        this.username=username;
        this.password=password;
        this.role=role;
        this.authorities=mapRoleToAuthorities(role);
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Integer role){
        UserRole userRole=UserRole.fromCode(role);
        String roleName=userRole.getRoleName();
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+roleName));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override  
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
