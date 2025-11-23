package com.example.park.domain.service.impl;

import com.example.park.common.PageResult;
import com.example.park.common.PageUtils;
import com.example.park.common.ResultCode;
import com.example.park.common.SystemException;
import com.example.park.domain.dto.CarAddRequestDTO;
import com.example.park.domain.dto.CarDeleteRequestDTO;
import com.example.park.domain.dto.CarPageResponseDTO;
import com.example.park.domain.dto.CarQueryRequestDTO;
import com.example.park.domain.dto.CarUpdateRequestDTO;
import com.example.park.domain.dto.CarUpdateResponseDTO;
import com.example.park.domain.dto.DropRequestDTO;
import com.example.park.domain.dto.RideRequestDTO;
import com.example.park.domain.entity.Car;
import com.example.park.domain.entity.CarUsage;
import com.example.park.domain.mapper.CarMapper;
import com.example.park.domain.mapper.CarUsageMapper;
import com.example.park.domain.mapper.StructMapper;
import com.example.park.domain.service.ICarService;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 高明(コウメイ)
 * @since 2025-10-15
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private CarUsageMapper carUsageMapper;

    @Autowired
    private StructMapper structMapper;

    @Override
    public void carAdd(CarAddRequestDTO dto,Integer role) {
        if (dto==null) {
            throw new SystemException(ResultCode.BAD_REQUEST);
        }
        Car car=structMapper.toEntityDTO(dto);
        carMapper.insert(car);
    }

    @Override
    public void carDelete(CarDeleteRequestDTO dto,Integer role) {
        if (dto==null) {
            throw new SystemException(ResultCode.BAD_REQUEST);
        }
        if(role!=1){
            throw new SystemException(ResultCode.ILLEGAL_PERMISSION);
        }
        UpdateWrapper<Car> wrapper=new UpdateWrapper<>();
        wrapper.in("id", dto.getIds())
               .set("is_deleted", 1);
        carMapper.update(null, wrapper);
    }

    @Override
    public PageResult<CarPageResponseDTO> pageInfo(CarQueryRequestDTO dto) {
        if(dto==null){
            throw new SystemException(ResultCode.BAD_REQUEST);
        }
        QueryWrapper<Car> wrapper=new QueryWrapper<>();
        if(StringUtils.hasText(dto.getCarNumber())){
            wrapper.like("car_number",dto.getCarNumber());
        }
        wrapper.last("ORDER BY FIELD(status,0,1),car_number ASC");

        Page<Car> page=new Page<>(dto.getPageNum(),dto.getPageSize());
        Page<Car> carPage=carMapper.selectPage(page, wrapper);

        return PageUtils.build(carPage, car -> structMapper.toDTO(car));
    }

    @Override
    public CarUpdateResponseDTO carUpdate(CarUpdateRequestDTO dto, Integer role) {
        if (dto==null) {
            throw new SystemException(ResultCode.BAD_REQUEST);
        }
        if(role!=1){
            throw new SystemException(ResultCode.ILLEGAL_PERMISSION);
        }
        Car car=carMapper.selectById(dto.getId());
        if(car.getIsDeleted()){
            throw new SystemException(ResultCode.NOT_FOUND);
        }
        structMapper.patchCar(dto, car);
        CarUpdateResponseDTO responseDTO=structMapper.toUpdateDTO(car);
        return responseDTO;
    }

    @Override
    @Transactional
    public void ride(RideRequestDTO dto,Long userId) {
        if (dto==null) {
            throw new SystemException(ResultCode.BAD_REQUEST);
        }
        if(dto.getUserId()!=userId){
            throw new SystemException(ResultCode.ILLEGAL_PERMISSION);
        }
        Car car=carMapper.selectById(dto.getCarId());
        if(car==null || car.getIsDeleted()){
            throw new SystemException(ResultCode.NOT_AVAILABLE);
        }
        //車輌のステータスを更新する
        car.setCurrentUserId(dto.getUserId());
        car.setStatus(1);
        carMapper.updateById(car);
        //履歴表に記入する
        CarUsage usage=structMapper.toEntity(dto);
        carUsageMapper.insert(usage);
    }

    @Override
    @Transactional
    public void drop(DropRequestDTO dto,Long userId) {
        if (dto==null) {
            throw new SystemException(ResultCode.BAD_REQUEST);
        }
        if(dto.getUserId()!=userId){
            throw new SystemException(ResultCode.ILLEGAL_PERMISSION);
        }
        Car car=carMapper.selectById(dto.getCarId());
        if(car==null || car.getIsDeleted()){
            throw new SystemException(ResultCode.NOT_AVAILABLE);
        }
        //車輌のステータスを更新する
        car.setCurrentUserId(null);
        car.setStatus(0);
        carMapper.updateById(car);

        //履歴表に記入する
        CarUsage usage=carUsageMapper.selectOne(
            new QueryWrapper<CarUsage>()
                .eq("car_id",dto.getCarId())
                .eq("user_id",dto.getUserId())
                .isNull("drop_time")
                .orderByDesc("ride_time")
                .last("LIMIT 1")
        );
        structMapper.patchDrop(dto, usage);
        carUsageMapper.updateById(usage);
    }

}
