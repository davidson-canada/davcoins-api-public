package com.davidson.davcoinsapi.repository;

import java.util.List;

import com.davidson.davcoinsapi.model.Product;
import com.davidson.davcoinsapi.model.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByTransactionDateDesc();
    Page<Transaction> findAllByProductOrderByTransactionDateDesc(Product product, Pageable pageable);
    Page<Transaction> findAllByOrderByTransactionDateDesc(Pageable pageable);
}
