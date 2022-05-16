package com.davidson.davcoinsapi.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    private String name;

    private String description = "";

    private BigDecimal price;

    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    private Timestamp modifiedDate = new Timestamp(System.currentTimeMillis());

}
