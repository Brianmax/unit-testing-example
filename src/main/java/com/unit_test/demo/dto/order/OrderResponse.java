package com.unit_test.demo.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.unit_test.demo.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private final UUID id;
    private final UUID customerId;
    private final OrderStatus status;
    private final List<OrderItemResponse> items;
    private final String discountCode;
    private final BigDecimal subtotal;
    private final BigDecimal discountAmount;
    private final BigDecimal total;
    private final Instant createdAt;
    private final Instant updatedAt;
}
