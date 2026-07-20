package com.unit_test.demo.dto.order;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDiscountRequest {

    @NotBlank
    private String discountCode;
}
