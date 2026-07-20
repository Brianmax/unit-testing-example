package com.unit_test.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unit_test.demo.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    Optional<Discount> findByCode(String code);

    boolean existsByCode(String code);
}
