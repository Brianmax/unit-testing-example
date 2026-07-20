package com.unit_test.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unit_test.demo.common.ApiResponse;
import com.unit_test.demo.dto.order.AddDiscountRequest;
import com.unit_test.demo.dto.order.CreateOrderRequest;
import com.unit_test.demo.dto.order.OrderResponse;
import com.unit_test.demo.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest httpRequest) {
        OrderResponse response = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirm(@PathVariable UUID id, HttpServletRequest httpRequest) {
        OrderResponse response = orderService.confirm(id);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @PutMapping("/{id}/discount")
    public ResponseEntity<ApiResponse<OrderResponse>> addDiscount(@PathVariable UUID id,
            @Valid @RequestBody AddDiscountRequest request, HttpServletRequest httpRequest) {
        OrderResponse response = orderService.addDiscount(id, request.getDiscountCode());
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @DeleteMapping("/{id}/discount")
    public ResponseEntity<ApiResponse<OrderResponse>> removeDiscount(@PathVariable UUID id,
            HttpServletRequest httpRequest) {
        OrderResponse response = orderService.removeDiscount(id);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable UUID id, HttpServletRequest httpRequest) {
        OrderResponse response = orderService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> list(HttpServletRequest httpRequest) {
        List<OrderResponse> response = orderService.list();
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }
}
