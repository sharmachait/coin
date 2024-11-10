package com.sharmachait.wazir.Model.Entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Roi {
    private BigDecimal times;
    private String currency;
    private BigDecimal percentage;
}