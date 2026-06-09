package com.balaji.distributor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErorResponse {

    private String message;

    private int status;

    private LocalDateTime timestamp;

}
