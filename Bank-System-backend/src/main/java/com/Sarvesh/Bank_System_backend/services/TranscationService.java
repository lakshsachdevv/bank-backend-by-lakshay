package com.Sarvesh.Bank_System_backend.services;

import com.Sarvesh.Bank_System_backend.dto.Transactiondto;
import com.Sarvesh.Bank_System_backend.entity.Transaction;

public interface TranscationService {
    void saveTransaction(Transactiondto transactiondto);

    void savedTransaction(Transaction transaction);

}
