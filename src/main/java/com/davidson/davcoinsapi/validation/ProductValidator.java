package com.davidson.davcoinsapi.validation;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.davidson.davcoinsapi.exception.InvalidProductException;
import com.davidson.davcoinsapi.model.Product;

import org.springframework.stereotype.Service;

@Service
public class ProductValidator {

    public void validateProduct(Product product) {
        if (product == null) {
            throw new InvalidProductException("Product cannot be null.");
        }

        if (product.getName() == null || product.getName().isBlank()) {
            throw new InvalidProductException(
                    "Invalid product name: '" + product.getName() + "'. Product name cannot be null or empty.");
        }

        if (product.getDescription() == null) {
            throw new InvalidProductException("Product description cannot be null.");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidProductException("Invalid product price: " + product.getPrice()
                    + ". Product price cannot be null or less than or equal to 0.");
        }

        if (product.getCreatedDate() == null
                || product.getCreatedDate().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
            throw new InvalidProductException("Invalid created date: " + product.getCreatedDate()
                    + ". Created date cannot be null or in the future.");
        }

        if (product.getModifiedDate() == null
                || product.getModifiedDate().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
            throw new InvalidProductException("Invalid modified date: " + product.getModifiedDate()
                    + ". Modified date cannot be null or in the future.");
        }
    }
}
