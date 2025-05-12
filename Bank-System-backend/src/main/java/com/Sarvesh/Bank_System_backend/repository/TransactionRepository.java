package com.Sarvesh.Bank_System_backend.repository;

import com.Sarvesh.Bank_System_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
}
