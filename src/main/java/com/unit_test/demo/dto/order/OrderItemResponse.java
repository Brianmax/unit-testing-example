package com.unit_test.demo.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemResponse {

    private final UUID productId;
    private final String sku;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPriceSnapshot;
    private final BigDecimal lineTotal;
}
