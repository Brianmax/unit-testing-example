package com.unit_test.demo.service;

import com.unit_test.demo.dto.discount.CreateDiscountRequest;
import com.unit_test.demo.dto.discount.DiscountResponse;
import com.unit_test.demo.entity.Discount;
import com.unit_test.demo.entity.DiscountType;
import com.unit_test.demo.exception.DuplicateResourceException;
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
import java.nio.file.attribute.DosFileAttributes;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


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
    // Crear y persistir todos los campos, incluido el maxDiscountAmount
    void create() {
        // act
        CreateDiscountRequest discountRequest = createRequest(DiscountType.PERCENTAGE);

        Discount discount = new Discount();
        when(discountRepository.existsByCode("SUMMER20")).thenReturn(false);
        when(discountRepository.save(any(Discount.class))).thenAnswer(invocation -> invocation.getArgument(0));


        DiscountResponse response = discountService.create(discountRequest);

        ArgumentCaptor<Discount> captor = ArgumentCaptor.forClass(Discount.class);
        verify(discountRepository).save(captor.capture());
        Discount persisted = captor.getValue();



        assertThat(persisted.getCode()).isEqualTo("SUMMER20");
        assertThat(persisted.getType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(persisted.getValue()).isEqualByComparingTo("20");
        assertThat(persisted.getMinOrderAmount()).isEqualByComparingTo("50");

        assertThat(response.getCode()).isEqualTo("SUMMER20");
    }

    @Test
    void createThrowsDuplicateResourceExceptionWhenCodeAlreadyExists() {
        CreateDiscountRequest request = createRequest(DiscountType.PERCENTAGE);
        when(discountRepository.existsByCode("SUMMER20")).thenReturn(true);

        assertThatThrownBy(() -> discountService.create(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(discountRepository, never()).save(any());
    }

    @Test
    void getByCode() {
        Discount discount = new Discount();
        discount.setCode("WELCOME10");
        discount.setType(DiscountType.PERCENTAGE);
        discount.setValue(new BigDecimal("10"));
        discount.setActive(true);

        when(discountRepository.findByCode("WELCOME10")).thenReturn(Optional.of(discount));

        DiscountResponse discountResponse = discountService.getByCode("WELCOME10");

        assertThat(discountResponse.getCode()).isEqualTo("WELCOME10");
        assertThat(discountResponse.getMaxDiscountAmount()).isNull();
    }

    @Test
    void getByCodeThrowsResourceNotFoundException() {

    }

    @Test
    void list() {
    }

    @Test
    void getEntityByCode() {
    }
}