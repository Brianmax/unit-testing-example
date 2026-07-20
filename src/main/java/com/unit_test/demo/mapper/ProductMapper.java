package com.unit_test.demo.mapper;

import com.unit_test.demo.dto.product.ProductResponse;
import com.unit_test.demo.entity.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getUnitPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}
