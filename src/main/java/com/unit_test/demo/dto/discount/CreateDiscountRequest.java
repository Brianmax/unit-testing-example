package com.unit_test.demo.dto.discount;

import java.math.BigDecimal;

import com.unit_test.demo.entity.DiscountType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDiscountRequest {

    @NotBlank
    @Size(max = 64)
    private String code;

    @NotNull
    private DiscountType type;

    @NotNull
    @Positive
    private BigDecimal value;

    @PositiveOrZero
    private BigDecimal minOrderAmount;

    @NotNull
    private Boolean active;
}
