package com.Sarvesh.Bank_System_backend.services;

import com.Sarvesh.Bank_System_backend.dto.Transactiondto;
import com.Sarvesh.Bank_System_backend.entity.Transaction;
import com.Sarvesh.Bank_System_backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionImpl implements TranscationService{

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transactiondto transactiondto)
    {
       Transaction transaction= Transaction.builder()
               .transactionType(transactiondto.getTransactionType())
               .accountNumber(transactiondto.getAccountNumber())
               .amount(transactiondto.getAmount())
               .status("SUCCESS")
               .build();
       transactionRepository.save(transaction);
       System.out.println(("Transaction saved Successfully"));
    }

    @Override
    public void savedTransaction(Transaction transaction) {

    }
}
