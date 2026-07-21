package com.unit_test.demo.mapper;

import com.unit_test.demo.dto.discount.DiscountResponse;
import com.unit_test.demo.entity.Discount;

public final class DiscountMapper {

    private DiscountMapper() {
    }

    public static DiscountResponse toResponse(Discount discount) {
        return new DiscountResponse(
                discount.getId(),
                discount.getCode(),
                discount.getType(),
                discount.getValue(),
                discount.getMinOrderAmount(),
                discount.getMaxDiscountAmount(),
                discount.isActive(),
                discount.getCreatedAt(),
                discount.getUpdatedAt());
    }
}
