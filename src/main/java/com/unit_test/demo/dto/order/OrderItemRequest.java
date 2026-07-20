package com.unit_test.demo.dto.order;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    @NotNull
    private UUID productId;

    @NotNull
    @Positive
    private Integer quantity;
}
