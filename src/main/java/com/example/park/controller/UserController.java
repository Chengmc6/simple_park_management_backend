package com.example.park.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.park.common.ApiResponse;
import com.example.park.domain.dto.UserInfoDTO;
import com.example.park.domain.dto.UserLoginRequestDTO;
import com.example.park.domain.dto.UserLoginResponseDTO;
import com.example.park.domain.dto.UserPasswordChangeDTO;
import com.example.park.domain.dto.UserRegisterDTO;
import com.example.park.domain.service.IUserService;
import com.example.park.security.CustomerUserDetails;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 高明(コウメイ)
 * @since 2025-10-15
 */
@RestController
@RequestMapping("/park/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final IUserService iService;

    public UserController(IUserService userService){
        this.iService=userService;
    }

    //新規登録インターフェース
    @PostMapping
    public ApiResponse<String> register(@RequestBody UserRegisterDTO dto){
        iService.register(dto);
        return ApiResponse.success(null,"登録が完了しました");
    }
    //ログインインターフェース
    @PostMapping("/login")
    public ApiResponse<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO dto){
        UserLoginResponseDTO responseDTO=iService.login(dto);
        return ApiResponse.success(responseDTO, "ログイン成功");
    }
    //ユーザーメッセージインターフェース
    @GetMapping("/me")
    public ApiResponse<UserInfoDTO> info(@AuthenticationPrincipal CustomerUserDetails userDetails){
        Long userId=userDetails.getUserId();
        UserInfoDTO dto=iService.info(userId);
        return ApiResponse.success(dto);
    }
    //パスワード変更インターフェース
    @PostMapping("/password_change")
    public ApiResponse<Void> changePassword(@RequestBody @Valid UserPasswordChangeDTO dto,
            @AuthenticationPrincipal CustomerUserDetails userDetails){
        Long userId=userDetails.getUserId();
        iService.changePassword(userId, dto);
        return ApiResponse.success();
    }
}

