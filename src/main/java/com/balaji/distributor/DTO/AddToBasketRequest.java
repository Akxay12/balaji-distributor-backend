package com.balaji.distributor.DTO;

import lombok.Data;

@Data
public class AddToBasketRequest {

    private Long productId;

    private Integer quantity;
}
