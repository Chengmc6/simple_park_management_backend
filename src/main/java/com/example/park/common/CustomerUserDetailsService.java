package com.example.park.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.park.domain.entity.User;
import com.example.park.domain.mapper.UserMapper;

@Service
public class CustomerUserDetailsService implements UserDetailsService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userMapper.selectByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException(username+"ユーザー名は存在しません");
        }
        return new CustomerUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

}
