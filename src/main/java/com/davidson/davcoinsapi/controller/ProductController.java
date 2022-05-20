package com.davidson.davcoinsapi.controller;

import com.davidson.davcoinsapi.dto.ProductDTO;
import com.davidson.davcoinsapi.model.Product;
import com.davidson.davcoinsapi.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createdProduct(@RequestBody ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<Product>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Iterable<Product>> getProductPage(@RequestParam int pageNumber, @RequestParam int pageSize){
        return new ResponseEntity<>(productService.getProductPage(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable long id){
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> modifyProduct(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        Product productToModify = productService.getProductById(id);

        productToModify.setName(productDTO.getName());
        productToModify.setDescription(productDTO.getDescription());
        productToModify.setPrice(productDTO.getPrice());

        return new ResponseEntity<>(productService.saveProduct(productToModify), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id){
        productService.deleteProductById(id);
    }
}
