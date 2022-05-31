package com.davidson.davcoinsapi.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.davidson.davcoinsapi.model.NotionUser;

import lombok.Data;

@Data
public class UserBalanceDTO implements Serializable {
    
    private static final long serialVersionUID = -1L;
    
    private NotionUser notionUser;

    private BigDecimal balance;

}
