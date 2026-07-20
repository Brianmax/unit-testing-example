package com.unit_test.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unit_test.demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT DISTINCT o FROM Order o "
            + "LEFT JOIN FETCH o.items i "
            + "LEFT JOIN FETCH i.product "
            + "LEFT JOIN FETCH o.discount "
            + "ORDER BY o.createdAt DESC")
    List<Order> findAllWithItems();
}
