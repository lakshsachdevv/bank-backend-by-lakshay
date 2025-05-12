package com.Sarvesh.Bank_System_backend.services;

import com.Sarvesh.Bank_System_backend.dto.*;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse creditaccount(creditdebitrequest request);

    BankResponse balanceEnquiry(EnquiryRequest request);
    BankResponse nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(creditdebitrequest request);
    BankResponse debitAccount(creditdebitrequest request);
    BankResponse transfer(transferrequest request);

    BankResponse login(Logindto logindto);
}
