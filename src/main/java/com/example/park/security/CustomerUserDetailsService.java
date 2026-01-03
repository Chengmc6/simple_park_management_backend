package com.example.park.security;


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
        // 额外的安全检查：确保密码哈希值不为空
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UsernameNotFoundException("ユーザー [" + username + "] 密码信息缺失。");
        }
        return new CustomerUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

}
