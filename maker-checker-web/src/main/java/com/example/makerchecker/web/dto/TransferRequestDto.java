package com.example.makerchecker.web.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferRequestDto {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String description;
    private String makerId;
}
