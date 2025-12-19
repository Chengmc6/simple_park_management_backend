package com.example.park.domain.service.impl;

import com.example.park.common.AuthenticationService;
import com.example.park.common.CustomerUserDetails;
import com.example.park.common.JwtUtils;
import com.example.park.common.ResultCode;
import com.example.park.common.SystemException;
import com.example.park.domain.converter.StructMapper;
import com.example.park.domain.dto.UserInfoDTO;
import com.example.park.domain.dto.UserLoginRequestDTO;
import com.example.park.domain.dto.UserLoginResponseDTO;
import com.example.park.domain.dto.UserPasswordChangeDTO;
import com.example.park.domain.dto.UserRegisterDTO;
import com.example.park.domain.entity.User;
import com.example.park.domain.mapper.UserMapper;
import com.example.park.domain.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 高明(コウメイ)
 * @since 2025-10-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final StructMapper structMapper;
    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserMapper userMapper,PasswordEncoder passwordEncoder,
                           StructMapper structMapper,AuthenticationService authenticationService){
        this.userMapper=userMapper;
        this.passwordEncoder=passwordEncoder;
        this.structMapper=structMapper;
        this.authenticationService=authenticationService;
    }

    @Override
    @Transactional
    public void register(UserRegisterDTO dto) {

        if(dto==null){
            throw new SystemException(ResultCode.BAD_REQUEST);
        }

        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        if(userMapper.selectCount(wrapper)>0){
            throw new SystemException(ResultCode.USERNAME_EXISTS);
        }
        //パスワードを暗号化する
        String encodePwd=passwordEncoder.encode(dto.getPassword());
        User user=new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encodePwd);
        user.setRole(0);
        user.setIsDeleted(false);

        userMapper.insert(user);
    }

    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO dto) {

        if(dto==null){
            throw new SystemException(ResultCode.BAD_REQUEST);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        User user = userMapper.selectOne(wrapper);

        // 2. 校验用户是否存在或是否被禁用
        if (user == null || user.getIsDeleted()) {
            // 使用通用的 LOGIN_FAILED 错误，避免向攻击者暴露是用户名还是密码错误
            throw new SystemException(ResultCode.LOGIN_FAILED);
        }

        // 3. 核心校验：使用 passwordEncoder 匹配明文密码和数据库中的哈希密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new SystemException(ResultCode.LOGIN_FAILED);
        }
        //ユーザーデータの認証
        CustomerUserDetails userDetails=authenticationService.authenticateUser(dto.getUsername(), dto.getPassword());
        //tokenを生成する
        String token=JwtUtils.getToken(userDetails.getId(), userDetails.getUsername(),userDetails.getRole());

        return new UserLoginResponseDTO(userDetails.getId(),userDetails.getUsername(),token);
    }

    @Override
    public UserInfoDTO info(Long userId) {
        User user=userMapper.selectById(userId);
        if(user==null){
            throw new SystemException(ResultCode.USER_NOT_FOUND);
        }
        UserInfoDTO dto=structMapper.toInfoDTO(user);
        return dto;
    }

    @Override
    @Transactional
    public void changePassword(Long userId,UserPasswordChangeDTO dto) {
        if (dto==null || userId==null) {
            throw new SystemException(ResultCode.BAD_REQUEST);
        }

        User user=userMapper.selectById(userId);
        if(user==null){
            throw new SystemException(ResultCode.USER_NOT_FOUND);
        }

        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())){
            throw new SystemException(ResultCode.PASSWORD_ERROR);
        }

        if(passwordEncoder.matches(dto.getNewPassword(), user.getPassword())){
            throw new SystemException(ResultCode.PASSWORD_SAME);
        }
        //新しいパスワードを暗号化する
        String newEnPassword=passwordEncoder.encode(dto.getNewPassword());
        User updatUser=new User();
        updatUser.setPassword(newEnPassword);
        updatUser.setId(userId);
        userMapper.updateById(updatUser);
    }

}
