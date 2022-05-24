package com.davidson.davcoinsapi.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "from_user", columnDefinition = "UUID NOT NULL")
    private UUID fromUser;

    @Column(name = "to_user", columnDefinition = "UUID NOT NULL")
    private UUID toUser;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_quantity", columnDefinition = "integer NOT NULL CHECK (product_quantity > 0)")
    private BigDecimal productQuantity;

    @Column(name = "transaction_amount", columnDefinition = "numeric(1000, 2) NOT NULL CHECK (transaction_amount > 0)")
    private BigDecimal transactionAmount;

    @Column(name = "transaction_date", columnDefinition = "Timestamp NOT NULL DEFAULT NOW()")
    private Timestamp transactionDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "transaction_description", columnDefinition = "text NOT NULL DEFAULT ''")
    private String transactionDescription = "";

}
