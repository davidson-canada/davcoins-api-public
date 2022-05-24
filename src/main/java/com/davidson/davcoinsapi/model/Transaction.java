package com.davidson.davcoinsapi.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_user", columnDefinition = "UUID NOT NULL")
    private UUID fromUser;

    @Column(name = "to_user", columnDefinition = "UUID NOT NULL")
    private UUID toUser;

    @Column(columnDefinition = "numeric(1000, 2) NOT NULL CHECK (amount > 0)")
    private BigDecimal amount;

    @Column(name = "transaction_date", columnDefinition = "Timestamp NOT NULL DEFAULT NOW()")
    private Timestamp transactionDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "transaction_description", columnDefinition = "text NOT NULL DEFAULT ''")
    private String transactionDescription = "";

    @Column(name = "transaction_reason", columnDefinition = "text NOT NULL DEFAULT ''")
    private String transactionReason = "";

}
