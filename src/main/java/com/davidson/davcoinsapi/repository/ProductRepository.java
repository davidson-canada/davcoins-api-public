package com.davidson.davcoinsapi.repository;

import com.davidson.davcoinsapi.model.Product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}
