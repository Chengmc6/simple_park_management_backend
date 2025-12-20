package com.example.park.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.park.common.ApiResponse;
import com.example.park.common.PageResult;
import com.example.park.domain.dto.UsageRequestDTO;
import com.example.park.domain.dto.UsageResponseVO;
import com.example.park.domain.service.ICarUsageService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 高明(コウメイ)
 * @since 2025-10-15
 */
@RestController
@RequestMapping("/park/car-usage")
public class CarUsageController {

    private final ICarUsageService usageService;

    public CarUsageController(ICarUsageService usageService){
        this.usageService=usageService;
    }

    @PostMapping("/history")
    public ApiResponse<PageResult<UsageResponseVO>> history(@RequestBody UsageRequestDTO dto){
        PageResult<UsageResponseVO> result=usageService.history(dto);
        return ApiResponse.success(result);
    }
}

