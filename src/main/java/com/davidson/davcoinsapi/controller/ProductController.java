package com.davidson.davcoinsapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.davidson.davcoinsapi.dto.ProductDTO;
import com.davidson.davcoinsapi.model.Product;
import com.davidson.davcoinsapi.service.ProductService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createdProduct(@RequestBody ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        return new ResponseEntity<>(convertToDTO(productService.saveProduct(product)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return new ResponseEntity<>(
                productService.getAllProducts().stream().map(this::convertToDTO).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductDTO>> getProductPage(@RequestParam int pageNumber, @RequestParam int pageSize) {
        Page<Product> productPage = productService.getProductPage(pageNumber, pageSize);

        Page<ProductDTO> productDTOPage = new PageImpl<>(productPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()),
        productPage.getPageable(), productPage.getTotalElements());

        return new ResponseEntity<>(productDTOPage, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> getProductSearchPage(@RequestParam int pageNumber, @RequestParam int pageSize,
            @RequestParam String query) {
        Page<Product> productPage = productService.getProductSearchPage(pageNumber, pageSize, query);

        Page<ProductDTO> productDTOPage = new PageImpl<>(productPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()),
        productPage.getPageable(), productPage.getTotalElements());

        return new ResponseEntity<>(productDTOPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long id) {
        return new ResponseEntity<>(convertToDTO(productService.getProductById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> modifyProduct(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        if(id != productDTO.getId().longValue()){
            throw new IllegalArgumentException("ID's do not match.");
        }

        Product product = convertToEntity(productDTO);

        return new ResponseEntity<>(convertToDTO(productService.saveProduct(product)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        productService.deleteProductById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private Product convertToEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }
}
