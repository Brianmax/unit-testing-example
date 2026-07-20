package com.unit_test.demo.mapper;

import com.unit_test.demo.dto.invoice.InvoiceResponse;
import com.unit_test.demo.entity.Invoice;

public final class InvoiceMapper {

    private InvoiceMapper() {
    }

    public static InvoiceResponse toResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getOrder().getId(),
                invoice.getSubtotal(),
                invoice.getDiscountAmount(),
                invoice.getTaxRate(),
                invoice.getTaxAmount(),
                invoice.getTotal(),
                invoice.getIssuedAt());
    }
}
