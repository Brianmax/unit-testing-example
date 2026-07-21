package com.unit_test.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unit_test.demo.dto.discount.CreateDiscountRequest;
import com.unit_test.demo.dto.discount.DiscountResponse;
import com.unit_test.demo.entity.Discount;
import com.unit_test.demo.exception.DuplicateResourceException;
import com.unit_test.demo.exception.ResourceNotFoundException;
import com.unit_test.demo.mapper.DiscountMapper;
import com.unit_test.demo.repository.DiscountRepository;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Transactional
    public DiscountResponse create(CreateDiscountRequest request) {
        if (discountRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Discount with code '" + request.getCode() + "' already exists");
        }

        Discount discount = new Discount();
        discount.setCode(request.getCode());
        discount.setType(request.getType());
        discount.setValue(request.getValue());
        discount.setMinOrderAmount(request.getMinOrderAmount());
        discount.setMaxDiscountAmount(request.getMaxDiscountAmount());
        discount.setActive(request.getActive());

        Discount saved = discountRepository.save(discount);
        return DiscountMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DiscountResponse getByCode(String code) {
        Discount discount = discountRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", code));
        return DiscountMapper.toResponse(discount);
    }

    @Transactional(readOnly = true)
    public List<DiscountResponse> list() {
        return discountRepository.findAll().stream()
                .map(DiscountMapper::toResponse)
                .toList();
    }

    Discount getEntityByCode(String code) {
        return discountRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", code));
    }
}
