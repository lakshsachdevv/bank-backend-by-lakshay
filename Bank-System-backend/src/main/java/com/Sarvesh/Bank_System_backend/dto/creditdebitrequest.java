package com.Sarvesh.Bank_System_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class creditdebitrequest {

    private String accountNumber;

    private BigDecimal amount;
}
