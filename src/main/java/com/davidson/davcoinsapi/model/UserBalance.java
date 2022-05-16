package com.davidson.davcoinsapi.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user_balance")
public class UserBalance {
    
    @Id
    private UUID id;

    @Column(columnDefinition = "numeric(1000, 2) NOT NULL CHECK (amount >= 0) DEFAULT 0")
    private BigDecimal balance;

}
