package com.unit_test.demo.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.unit_test.demo.dto.order.OrderItemResponse;
import com.unit_test.demo.dto.order.OrderResponse;
import com.unit_test.demo.entity.Order;
import com.unit_test.demo.entity.OrderItem;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        return toResponse(order, order.getSubtotal(), order.getDiscountAmount(), order.getTotal());
    }

    public static OrderResponse toResponse(Order order, BigDecimal subtotal, BigDecimal discountAmount,
            BigDecimal total) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderMapper::toItemResponse)
                .toList();

        String discountCode = order.getDiscount() != null ? order.getDiscount().getCode() : null;

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                items,
                discountCode,
                subtotal,
                discountAmount,
                total,
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    public static OrderItemResponse toItemResponse(OrderItem item) {
        BigDecimal lineTotal = item.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getSku(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPriceSnapshot(),
                lineTotal);
    }
}
