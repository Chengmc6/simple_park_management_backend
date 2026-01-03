package com.example.park.domain.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.park.security.CustomerUserDetails;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
    }

    public CustomerUserDetails authenticateUser(String username,String password){
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication=authenticationManager.authenticate(authenticationToken);
        CustomerUserDetails userDetails=(CustomerUserDetails) authentication.getPrincipal();
        return userDetails;
    }
}
