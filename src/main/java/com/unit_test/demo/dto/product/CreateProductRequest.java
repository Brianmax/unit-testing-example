package com.unit_test.demo.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank
    @Size(max = 64)
    private String sku;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    @Positive
    private BigDecimal unitPrice;
}
