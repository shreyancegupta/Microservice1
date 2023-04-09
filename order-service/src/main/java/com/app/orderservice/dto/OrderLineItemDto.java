package com.app.orderservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderLineItemDto {
    
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
