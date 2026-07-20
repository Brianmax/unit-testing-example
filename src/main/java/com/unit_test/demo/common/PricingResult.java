package com.unit_test.demo.common;

import java.math.BigDecimal;

public record PricingResult(BigDecimal subtotal, BigDecimal discountAmount) {

    public BigDecimal discountedTotal() {
        return subtotal.subtract(discountAmount);
    }
}
