package com.unit_test.demo.dto.invoice;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvoiceResponse {

    private final UUID id;
    private final UUID orderId;
    private final BigDecimal subtotal;
    private final BigDecimal discountAmount;
    private final BigDecimal taxRate;
    private final BigDecimal taxAmount;
    private final BigDecimal total;
    private final Instant issuedAt;
}
