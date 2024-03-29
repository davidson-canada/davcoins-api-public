package com.davidson.davcoinsapi.repository;

import com.davidson.davcoinsapi.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Iterable<Product> findAllByOrderByNameAsc();
    Page<Product> findAllByOrderByNameAsc(Pageable pageable);
    Page<Product> findAllByNameIgnoreCaseContainingOrderByNameAsc(String name, Pageable pageable);
}
