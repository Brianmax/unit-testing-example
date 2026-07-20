package com.unit_test.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unit_test.demo.common.LineItem;
import com.unit_test.demo.common.PricingCalculator;
import com.unit_test.demo.common.PricingResult;
import com.unit_test.demo.dto.order.CreateOrderRequest;
import com.unit_test.demo.dto.order.OrderItemRequest;
import com.unit_test.demo.dto.order.OrderResponse;
import com.unit_test.demo.entity.Discount;
import com.unit_test.demo.entity.Order;
import com.unit_test.demo.entity.OrderItem;
import com.unit_test.demo.entity.OrderStatus;
import com.unit_test.demo.entity.Product;
import com.unit_test.demo.exception.InvalidDiscountException;
import com.unit_test.demo.exception.InvalidOrderStateException;
import com.unit_test.demo.exception.ResourceNotFoundException;
import com.unit_test.demo.mapper.OrderMapper;
import com.unit_test.demo.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final DiscountService discountService;
    private final PricingCalculator pricingCalculator;

    public OrderService(OrderRepository orderRepository, ProductService productService,
            DiscountService discountService, PricingCalculator pricingCalculator) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.discountService = discountService;
        this.pricingCalculator = pricingCalculator;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(OrderStatus.PENDING);

        if (request.getDiscountCode() != null && !request.getDiscountCode().isBlank()) {
            Discount discount = discountService.getEntityByCode(request.getDiscountCode());
            if (!discount.isActive()) {
                throw new InvalidDiscountException("Discount code is not active: " + request.getDiscountCode());
            }
                            order.setDiscount(discount);
        }

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.getEntityById(itemRequest.getProductId());

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPriceSnapshot(product.getUnitPrice());
            order.addItem(item);
        }

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Transactional
    public OrderResponse confirm(UUID orderId) {
        Order order = getEntityById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException(
                    "Order must be PENDING to confirm, current status: " + order.getStatus());
        }

        PricingResult result = calculatePricing(order);

        order.setSubtotal(result.subtotal());
        order.setDiscountAmount(result.discountAmount());
        order.setTotal(result.discountedTotal());
        order.setStatus(OrderStatus.CONFIRMED);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Transactional
    public OrderResponse addDiscount(UUID orderId, String discountCode) {
        Order order = getEntityById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException(
                    "Order must be PENDING to add a discount, current status: " + order.getStatus());
        }

        Discount discount = discountService.getEntityByCode(discountCode);
        if (!discount.isActive()) {
            throw new InvalidDiscountException("Discount code is not active: " + discountCode);
        }
        order.setDiscount(discount);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Transactional
    public OrderResponse removeDiscount(UUID orderId) {
        Order order = getEntityById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException(
                    "Order must be PENDING to remove a discount, current status: " + order.getStatus());
        }

        order.setDiscount(null);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(UUID id) {
        return toResponse(getEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAllWithItems().stream()
                .map(this::toResponse)
                .toList();
    }

    Order getEntityById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    private PricingResult calculatePricing(Order order) {
        List<LineItem> lineItems = order.getItems().stream()
                .map(item -> new LineItem(item.getUnitPriceSnapshot(), item.getQuantity()))
                .toList();
        return pricingCalculator.calculate(lineItems, order.getDiscount());
    }

    private OrderResponse toResponse(Order order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            return OrderMapper.toResponse(order);
        }

        PricingResult preview = calculatePricing(order);
        return OrderMapper.toResponse(order, preview.subtotal(), preview.discountAmount(),
                preview.discountedTotal());
    }
}
