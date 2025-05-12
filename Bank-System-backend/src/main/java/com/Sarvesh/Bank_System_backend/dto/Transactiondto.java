package com.Sarvesh.Bank_System_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transactiondto {

    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
