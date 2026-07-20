package com.unit_test.demo.dto.discount;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.unit_test.demo.entity.DiscountType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiscountResponse {

    private final UUID id;
    private final String code;
    private final DiscountType type;
    private final BigDecimal value;
    private final BigDecimal minOrderAmount;
    private final boolean active;
    private final Instant createdAt;
    private final Instant updatedAt;
}
