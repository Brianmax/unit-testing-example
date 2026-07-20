package com.unit_test.demo.dto.order;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {

    @NotNull
    private UUID customerId;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    private String discountCode;
}
