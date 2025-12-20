package com.example.park.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.park.common.ApiResponse;
import com.example.park.common.CustomerUserDetails;
import com.example.park.common.PageResult;
import com.example.park.domain.dto.CarAddRequestDTO;
import com.example.park.domain.dto.CarDeleteRequestDTO;
import com.example.park.domain.dto.CarPageResponseDTO;
import com.example.park.domain.dto.CarQueryRequestDTO;
import com.example.park.domain.dto.CarUpdateRequestDTO;
import com.example.park.domain.dto.CarUpdateResponseDTO;
import com.example.park.domain.dto.DropRequestDTO;
import com.example.park.domain.dto.RideRequestDTO;
import com.example.park.domain.service.ICarService;

import jakarta.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 高明(コウメイ)
 * @since 2025-10-15
 */
@RestController
@RequestMapping("/park/car")
public class CarController {

    private final ICarService iCarService;

    public CarController(ICarService iCarService){
        this.iCarService=iCarService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> carAdd(@RequestBody @Valid CarAddRequestDTO dto){
        iCarService.carAdd(dto);
        return ApiResponse.success();
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> carDelete(@RequestBody @Valid CarDeleteRequestDTO dto){
        iCarService.carDelete(dto);
        return ApiResponse.success();
    }

    @PostMapping("/query")
    public ApiResponse<PageResult<CarPageResponseDTO>> query(@RequestBody CarQueryRequestDTO dto){
        return ApiResponse.success(iCarService.pageInfo(dto));
    }

    @PostMapping("/update")
    public ApiResponse<CarUpdateResponseDTO> update(@RequestBody @Valid CarUpdateRequestDTO dto){
        CarUpdateResponseDTO responseDTO=iCarService.carUpdate(dto);
        return ApiResponse.success(responseDTO);
    }

    @PostMapping("/ride")
    public ApiResponse<Void> ride(@RequestBody @Valid RideRequestDTO dto,
            @AuthenticationPrincipal CustomerUserDetails userDetails){
        iCarService.ride(dto, userDetails.getUserId());
        return ApiResponse.success();
    }

    @PostMapping("/drop")
    public ApiResponse<Void> drop(@RequestBody @Valid DropRequestDTO dto,
            @AuthenticationPrincipal CustomerUserDetails userDetails){
        iCarService.drop(dto, userDetails.getUserId());
        return ApiResponse.success();
    }
}

