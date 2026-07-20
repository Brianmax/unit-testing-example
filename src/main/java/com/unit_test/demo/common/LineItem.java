package com.unit_test.demo.common;

import java.math.BigDecimal;

public record LineItem(BigDecimal unitPrice, int quantity) {
}
