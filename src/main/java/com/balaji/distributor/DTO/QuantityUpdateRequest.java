package com.balaji.distributor.DTO;


import lombok.Data;

@Data
public class QuantityUpdateRequest {

    private Long basketId;

    private Long productId;

    private Integer quantity;

}
