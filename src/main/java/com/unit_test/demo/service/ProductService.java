package com.unit_test.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unit_test.demo.dto.product.CreateProductRequest;
import com.unit_test.demo.dto.product.ProductResponse;
import com.unit_test.demo.entity.Product;
import com.unit_test.demo.exception.DuplicateResourceException;
import com.unit_test.demo.exception.ResourceNotFoundException;
import com.unit_test.demo.mapper.ProductMapper;
import com.unit_test.demo.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with sku '" + request.getSku() + "' already exists");
        }

        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setUnitPrice(request.getUnitPrice());

        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return ProductMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    Product getEntityById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }
}
