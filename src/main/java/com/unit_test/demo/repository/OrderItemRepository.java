package com.unit_test.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unit_test.demo.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
