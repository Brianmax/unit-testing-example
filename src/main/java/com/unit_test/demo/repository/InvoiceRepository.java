package com.unit_test.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unit_test.demo.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    boolean existsByOrderId(UUID orderId);

    @Query("SELECT i FROM Invoice i ORDER BY i.createdAt DESC")
    List<Invoice> findAllOrderByCreatedAtDesc();
}
