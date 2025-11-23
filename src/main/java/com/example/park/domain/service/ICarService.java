package com.example.park.domain.service;

import com.example.park.common.PageResult;
import com.example.park.domain.dto.CarAddRequestDTO;
import com.example.park.domain.dto.CarDeleteRequestDTO;
import com.example.park.domain.dto.CarPageResponseDTO;
import com.example.park.domain.dto.CarQueryRequestDTO;
import com.example.park.domain.dto.CarUpdateRequestDTO;
import com.example.park.domain.dto.CarUpdateResponseDTO;
import com.example.park.domain.dto.DropRequestDTO;
import com.example.park.domain.dto.RideRequestDTO;
import com.example.park.domain.entity.Car;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 高明(コウメイ)
 * @since 2025-10-15
 */
public interface ICarService extends IService<Car> {
    void carAdd(CarAddRequestDTO dto,Integer role);
    void carDelete(CarDeleteRequestDTO dto,Integer role);
    PageResult<CarPageResponseDTO> pageInfo(CarQueryRequestDTO dto);
    CarUpdateResponseDTO carUpdate(CarUpdateRequestDTO dto,Integer role);
    void ride(RideRequestDTO dto,Long userId);
    void drop(DropRequestDTO dto,Long userId);
}
