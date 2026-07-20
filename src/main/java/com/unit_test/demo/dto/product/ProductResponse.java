package com.unit_test.demo.dto.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private final UUID id;
    private final String sku;
    private final String name;
    private final BigDecimal unitPrice;
    private final Instant createdAt;
    private final Instant updatedAt;
}
