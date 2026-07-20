package com.unit_test.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unit_test.demo.common.ApiResponse;
import com.unit_test.demo.dto.invoice.InvoiceResponse;
import com.unit_test.demo.service.InvoiceService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/api/orders/{orderId}/invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> generate(@PathVariable UUID orderId,
            HttpServletRequest httpRequest) {
        InvoiceResponse response = invoiceService.generate(orderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @GetMapping("/api/invoices/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(@PathVariable UUID id,
            HttpServletRequest httpRequest) {
        InvoiceResponse response = invoiceService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }

    @GetMapping("/api/invoices")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> list(HttpServletRequest httpRequest) {
        List<InvoiceResponse> response = invoiceService.list();
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestURI()));
    }
}
