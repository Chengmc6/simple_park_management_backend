package com.example.park.common;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomerUserDetails implements UserDetails{
    private Long id;
    private String username;
    private String password;
    private Integer role;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomerUserDetails(Long id,String username,String password,Integer role){
        this.id=id;
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
