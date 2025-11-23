package com.example.park.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.park.domain.dto.CarAddRequestDTO;
import com.example.park.domain.dto.CarPageResponseDTO;
import com.example.park.domain.dto.CarUpdateRequestDTO;
import com.example.park.domain.dto.CarUpdateResponseDTO;
import com.example.park.domain.dto.DropRequestDTO;
import com.example.park.domain.dto.RideRequestDTO;
import com.example.park.domain.dto.UserInfoDTO;
import com.example.park.domain.entity.Car;
import com.example.park.domain.entity.CarUsage;
import com.example.park.domain.entity.User;


@Mapper(componentModel = "spring")
public interface StructMapper {

    UserInfoDTO toInfoDTO(User user);
    
    Car toEntityDTO(CarAddRequestDTO dto);

    CarPageResponseDTO toDTO(Car car);

    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    void patchCar(CarUpdateRequestDTO dto,@MappingTarget Car car);
    CarUpdateResponseDTO toUpdateDTO(Car car);

    CarUsage toEntity(RideRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchDrop(DropRequestDTO dto,@MappingTarget CarUsage usage);
}
