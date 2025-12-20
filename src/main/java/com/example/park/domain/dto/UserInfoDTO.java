package com.example.park.domain.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
}
