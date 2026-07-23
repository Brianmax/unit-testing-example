package com.unit_test.demo.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Crear ordenes, aplicar descuentos y confirmar")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Crear ordenes en estado PENDING", description = "Crea ordenes en estado pendiente desde un customerId," +
            " puede ser mas de un producto, y el codigo de descuento es opcional")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",
            description = "Orden creada con exito. Status PENDING",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "createdOrder",
                            summary = "Orden creada exitosamente",
                            value = "{\n  \"success\": true,\n  \"data\": {\n    \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n    \"customerId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa7\",\n    \"status\": \"PENDING\",\n    \"items\": [{\n      \"productId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa8\",\n      \"sku\": \"LAP-001\",\n      \"productName\": \"Laptop Gamer\",\n      \"quantity\": 1,\n      \"unitPriceSnapshot\": 1200.00,\n      \"lineTotal\": 1200.00\n    }],\n    \"discountCode\": \"SUMMER10\",\n    \"subtotal\": 1200.00,\n    \"discountAmount\": 120.00,\n    \"total\": 1080.00,\n    \"createdAt\": \"2024-01-15T10:30:00Z\",\n    \"updatedAt\": \"2024-01-15T10:30:00Z\"\n  },\n  \"error\": null,\n  \"timestamp\": \"2024-01-15T10:30:00Z\",\n  \"path\": \"/api/orders\"\n}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
            description = "Validacion fallida, o el descuento existe, pero no esta activo",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = {
                            @ExampleObject(name = "validationError",
                                    summary = "Error de validacion de entrada",
                                    value = "{\n  \"success\": false,\n  \"data\": null,\n  \"error\": {\n    \"code\": \"VALIDATION_FAILED\",\n    \"message\": \"Input validation failed\",\n    \"details\": {\n      \"customerId\": \"must not be null\",\n      \"items\": \"must not be empty\"\n    }\n  },\n  \"timestamp\": \"2024-01-15T10:30:00Z\",\n  \"path\": \"/api/orders\"\n}"),
                            @ExampleObject(name = "invalidDiscount",
                                    summary = "Codigo de descuento no activo",
                                    value = "{\n  \"success\": false,\n  \"data\": null,\n  \"error\": {\n    \"code\": \"INVALID_DISCOUNT\",\n    \"message\": \"Discount code SUMMER10 is not active\",\n    \"details\": null\n  },\n  \"timestamp\": \"2024-01-15T10:30:00Z\",\n  \"path\": \"/api/orders\"\n}")
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
            description = "Algun producto o codigo de descuento no existe",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "resourceNotFound",
                            summary = "Producto o descuento no encontrado",
                            value = "{\n  \"success\": false,\n  \"data\": null,\n  \"error\": {\n    \"code\": \"RESOURCE_NOT_FOUND\",\n    \"message\": \"Product not found: 3fa85f64-5717-4562-b3fc-2c963f66afa8\",\n    \"details\": null\n  },\n  \"timestamp\": \"2024-01-15T10:30:00Z\",\n  \"path\": \"/api/orders\"\n}")))
    })
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
