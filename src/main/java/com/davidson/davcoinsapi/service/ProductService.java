package com.davidson.davcoinsapi.service;

import java.sql.Timestamp;

import com.davidson.davcoinsapi.exception.InvalidProductException;
import com.davidson.davcoinsapi.exception.ProductNotFoundException;
import com.davidson.davcoinsapi.model.Product;
import com.davidson.davcoinsapi.repository.ProductRepository;
import com.davidson.davcoinsapi.validation.ProductValidator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductValidator productValidator;

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(final long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
    }

    public Product saveProduct(final Product product) throws InvalidProductException {
        product.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        productValidator.validateProduct(product);
        return productRepository.save(product);
    }

    public void deleteProductById(final long id) {
        productRepository.deleteById(id);
    }

}
