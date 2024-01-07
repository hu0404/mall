package com.pro.product.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPrice {
    private long id;
    private String name;
    private BigDecimal price;
}
