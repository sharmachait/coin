package com.sharmachait.wazir.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coin_id", referencedColumnName = "id", nullable=false)
    private Coin coin;

    private double quantity;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;

}
