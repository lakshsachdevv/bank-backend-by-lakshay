package com.Sarvesh.Bank_System_backend.controller;

import com.Sarvesh.Bank_System_backend.entity.Transaction;
import com.Sarvesh.Bank_System_backend.services.BankStatement;
import com.Sarvesh.Bank_System_backend.services.TransactionImpl;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {

    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }

}
