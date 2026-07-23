package com.unit_test.demo.service;

import com.unit_test.demo.dto.discount.CreateDiscountRequest;
import com.unit_test.demo.dto.discount.DiscountResponse;
import com.unit_test.demo.entity.Discount;
import com.unit_test.demo.entity.DiscountType;
import com.unit_test.demo.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;


    @BeforeEach
    void setUp() {
        discountService = new DiscountService(discountRepository);
    }

    private static CreateDiscountRequest createRequest(DiscountType discountType) {
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("SUMMER20");
        request.setType(discountType);
        request.setValue(new BigDecimal("30"));
        request.setMinOrderAmount(new BigDecimal("50"));
        request.setMaxDiscountAmount(new BigDecimal("100"));
        request.setActive(true);
        return request;
    }

    @Test
    // Crear y persistir todos los campos, incluidos el maxDiscountAmount
    void create() {
        // act
        CreateDiscountRequest discountRequest = createRequest(DiscountType.PERCENTAGE);

        Discount discount = new Discount();
        when(discountRepository.existsByCode("SUMMER20")).thenReturn(false);
        when(discountRepository.save(any(Discount.class))).thenAnswer(invocation -> invocation.getArgument(0));


        DiscountResponse response = discountService.create(discountRequest);

    }

    @Test
    void getByCode() {
    }

    @Test
    void list() {
    }

    @Test
    void getEntityByCode() {
    }
}