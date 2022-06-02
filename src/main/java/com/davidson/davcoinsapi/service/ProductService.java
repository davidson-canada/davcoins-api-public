package com.davidson.davcoinsapi.service;

import java.sql.Timestamp;
import java.util.List;

import com.davidson.davcoinsapi.exception.InvalidProductException;
import com.davidson.davcoinsapi.exception.ProductNotFoundException;
import com.davidson.davcoinsapi.model.Product;
import com.davidson.davcoinsapi.repository.ProductRepository;
import com.davidson.davcoinsapi.validation.ProductValidator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductValidator productValidator;

    public List<Product> getAllProducts() {
        return productRepository.findAllByOrderByNameAsc();
    }

    public Page<Product> getProductPage(final int pageNumber, final int pageSize) {
        return productRepository.findAllByOrderByNameAsc(PageRequest.of(pageNumber-1, pageSize));
    }

    public Page<Product> getProductSearchPage(final int pageNumber, final int pageSize, String query) {
        return productRepository.findAllByNameIgnoreCaseContainingOrderByNameAsc(query, PageRequest.of(pageNumber-1, pageSize));
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
