package com.davidson.davcoinsapi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.davidson.davcoinsapi.model.NotionUser;

import lombok.Data;

@Data
public class TransactionDTO implements Serializable {
    
    private static final long serialVersionUID = -1L;
    
    private Long id;

    private NotionUser fromUser;

    private NotionUser toUser;

    private ProductDTO product;

    private BigDecimal productQuantity;

    private BigDecimal transactionAmount;

    private Timestamp transactionDate = new Timestamp(System.currentTimeMillis());

    private String transactionDescription = "";
}
