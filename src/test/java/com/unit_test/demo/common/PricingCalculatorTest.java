package com.unit_test.demo.common;

import com.unit_test.demo.entity.Discount;
import com.unit_test.demo.entity.DiscountType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;


class PricingCalculatorTest {
    private final PricingCalculator calculator = new PricingCalculator();

    private static Discount createDiscount(DiscountType type, BigDecimal value, BigDecimal maxDiscountAmount) {
        Discount discount = new Discount();
        discount.setType(type);
        discount.setValue(value);
        discount.setMaxDiscountAmount(maxDiscountAmount);
        discount.setActive(true);
        return discount;
    }

    private static List<LineItem> lineItems(BigDecimal precio) {
        return List.of(new LineItem(precio, 1));
    }
    @Test
    // descuento sale menor a la cantidad que se tiene que pagar
    // happy-path
    void percentageDiscount() {
        // Assert
        Discount discount = createDiscount(
                DiscountType.PERCENTAGE,
                new BigDecimal("50"),
                new BigDecimal("100")
        );
        List<LineItem> lineItems = lineItems(new BigDecimal("150"));
        PricingResult result = calculator.calculate(lineItems, discount);

        assertThat(result.discountAmount()).isEqualByComparingTo("75");
    }

    @Test
    // Descuento sale mayor a la cantidad que se tiene que pagar y existe un descuento maximo
    void percentageDiscountAboveCapIsLimitedMaxDiscountAmount() {
        Discount discount = createDiscount(
                DiscountType.PERCENTAGE,
                new BigDecimal("20"),
                new BigDecimal("100")
                );
        List<LineItem> lineItems = lineItems(new BigDecimal("1000"));

        PricingResult pricingResult = calculator.calculate(lineItems, discount);

        assertThat(pricingResult.discountAmount()).isEqualByComparingTo("100");
        assertThat(pricingResult.discountedTotal()).isEqualByComparingTo("900");
    }
    @Test
    void fixedAmountCalculate() {
        Discount discount = new Discount();
        discount.setType(DiscountType.FIXED_AMOUNT);
        discount.setValue(new BigDecimal("50"));
        discount.setActive(true);

        List<LineItem> lineItems = lineItems(new BigDecimal("400"));

        PricingResult pricingResult = calculator.calculate(lineItems, discount);

        assertThat(pricingResult.discountAmount()).isEqualByComparingTo("50");
        assertThat(pricingResult.discountedTotal()).isEqualByComparingTo("350");
    }
}