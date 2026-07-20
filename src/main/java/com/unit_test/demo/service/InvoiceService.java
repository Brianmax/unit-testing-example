package com.unit_test.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unit_test.demo.common.LineItem;
import com.unit_test.demo.common.PricingCalculator;
import com.unit_test.demo.common.PricingResult;
import com.unit_test.demo.dto.invoice.InvoiceResponse;
import com.unit_test.demo.entity.Invoice;
import com.unit_test.demo.entity.Order;
import com.unit_test.demo.entity.OrderStatus;
import com.unit_test.demo.exception.DuplicateResourceException;
import com.unit_test.demo.exception.InvalidOrderStateException;
import com.unit_test.demo.exception.ResourceNotFoundException;
import com.unit_test.demo.mapper.InvoiceMapper;
import com.unit_test.demo.repository.InvoiceRepository;

@Service
public class InvoiceService {

    private static final int MONEY_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final InvoiceRepository invoiceRepository;
    private final OrderService orderService;
    private final PricingCalculator pricingCalculator;
    private final BigDecimal taxRate;

    public InvoiceService(InvoiceRepository invoiceRepository, OrderService orderService,
            PricingCalculator pricingCalculator, @Value("${app.invoice.tax-rate}") BigDecimal taxRate) {
        this.invoiceRepository = invoiceRepository;
        this.orderService = orderService;
        this.pricingCalculator = pricingCalculator;
        this.taxRate = taxRate;
    }

    @Transactional
    public InvoiceResponse generate(UUID orderId) {
        Order order = orderService.getEntityById(orderId);

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException("Order must be CONFIRMED to generate an invoice");
        }

        if (invoiceRepository.existsByOrderId(orderId)) {
            throw new DuplicateResourceException("Invoice already exists for order " + orderId);
        }

        List<LineItem> lineItems = order.getItems().stream()
                .map(item -> new LineItem(item.getUnitPriceSnapshot(), item.getQuantity()))
                .toList();

        PricingResult result = pricingCalculator.calculate(lineItems, order.getDiscount());

        BigDecimal discountedTotal = result.discountedTotal();
        BigDecimal taxAmount = discountedTotal.multiply(taxRate).setScale(MONEY_SCALE, ROUNDING_MODE);
        BigDecimal total = discountedTotal.add(taxAmount).setScale(MONEY_SCALE, ROUNDING_MODE);

        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setSubtotal(result.subtotal());
        invoice.setDiscountAmount(result.discountAmount());
        invoice.setTaxRate(taxRate);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotal(total);
        invoice.setIssuedAt(Instant.now());

        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", id));
        return InvoiceMapper.toResponse(invoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> list() {
        return invoiceRepository.findAllOrderByCreatedAtDesc().stream()
                .map(InvoiceMapper::toResponse)
                .toList();
    }
}
