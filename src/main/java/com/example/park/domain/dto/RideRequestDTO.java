package com.example.park.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RideRequestDTO {
    private Long carId;
    private Long userId;
    private LocalDateTime rideTime;
    private BigDecimal rideAlcoholLevel;
}
