package com.davidson.davcoinsapi.repository;

import java.util.List;

import com.davidson.davcoinsapi.model.Transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    
    List<Transaction> findAllByOrderByTransactionDateDesc();

}
