package com.unit_test.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unit_test.demo.common.ApiResponse;
import com.unit_test.demo.dto.discount.CreateDiscountRequest;
import com.unit_test.demo.dto.discount.DiscountResponse;
import com.unit_test.demo.service.DiscountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DiscountResponse>> create(@Valid @RequestBody CreateDiscountRequest request,
            HttpServletRequest httpRequest) {
        DiscountResponse response = discountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getByCode(@PathVariable String code,
            HttpServletRequest httpRequest) {
        DiscountResponse response = discountService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> list(HttpServletRequest httpRequest) {
        List<DiscountResponse> response = discountService.list();
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }
}
