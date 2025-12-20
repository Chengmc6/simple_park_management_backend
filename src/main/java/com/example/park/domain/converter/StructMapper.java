package com.example.park.domain.converter;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mapping(target = "createdAT", source = "createdAt")
    UserInfoDTO toInfoDTO(User user);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "currentUserId", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Car toEntityDTO(CarAddRequestDTO dto);

    CarPageResponseDTO toDTO(Car car);

    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "currentUserId", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void patchCar(CarUpdateRequestDTO dto,@MappingTarget Car car);
    CarUpdateResponseDTO toUpdateDTO(Car car);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dropTime", ignore = true)
    @Mapping(target = "dropAlcoholLevel", ignore = true)
    @Mapping(target = "carId", source = "carId")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "rideTime", source = "rideTime")
    @Mapping(target = "rideAlcoholLevel", source = "rideAlcoholLevel")
    CarUsage toEntity(RideRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rideTime", ignore = true)
    @Mapping(target = "rideAlcoholLevel", ignore = true)
    @Mapping(target = "dropTime", source = "dropTime")
    @Mapping(target = "dropAlcoholLevel", source = "dropAlcoholLevel")
    void patchDrop(DropRequestDTO dto,@MappingTarget CarUsage usage);
}
