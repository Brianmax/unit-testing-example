package com.unit_test.demo.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

import com.unit_test.demo.entity.Discount;

@Component
public class PricingCalculator {

    private static final int MONEY_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public PricingResult calculate(List<LineItem> lineItems, Discount discount) {
        BigDecimal subtotal = lineItems.stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(MONEY_SCALE, ROUNDING_MODE);

        BigDecimal discountAmount = calculateDiscountAmount(subtotal, discount);

        return new PricingResult(subtotal, discountAmount);
    }

    private BigDecimal calculateDiscountAmount(BigDecimal subtotal, Discount discount) {
        if (discount == null || !discount.isActive()) {
            return BigDecimal.ZERO.setScale(MONEY_SCALE, ROUNDING_MODE);
        }

        if (discount.getMinOrderAmount() != null && subtotal.compareTo(discount.getMinOrderAmount()) < 0) {
            return BigDecimal.ZERO.setScale(MONEY_SCALE, ROUNDING_MODE);
        }

        BigDecimal rawDiscount = switch (discount.getType()) {
            case PERCENTAGE -> subtotal.multiply(discount.getValue())
                    .divide(BigDecimal.valueOf(100), MONEY_SCALE, ROUNDING_MODE);
            case FIXED_AMOUNT -> discount.getValue();
        };

        BigDecimal capped = rawDiscount.min(subtotal);
        return capped.max(BigDecimal.ZERO).setScale(MONEY_SCALE, ROUNDING_MODE);
    }
}
