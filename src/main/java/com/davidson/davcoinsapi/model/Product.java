package com.davidson.davcoinsapi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product implements Serializable {

	private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NOT NULL CHECK (name <> '')")
    private String name;

    @Column(columnDefinition = "NOT NULL DEFAULT ''")
    private String description = "";

    @Column(columnDefinition = "NOT NULL CHECK (price >= 0)")
    private BigDecimal price;

    @Column(name = "created_date", columnDefinition = "NOT NULL DEFAULT NOW()")
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "modified_date", columnDefinition = "NOT NULL DEFAULT NOW()")
    private Timestamp modifiedDate = new Timestamp(System.currentTimeMillis());
}
