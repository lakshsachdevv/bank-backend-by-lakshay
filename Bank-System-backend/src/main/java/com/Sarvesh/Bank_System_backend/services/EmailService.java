package com.Sarvesh.Bank_System_backend.services;

import com.Sarvesh.Bank_System_backend.dto.EmailDetails;

public interface EmailService {
  void setMailAlert(EmailDetails emailDetails);
  void sendEmailwithAttachment(EmailDetails emailDetails);
}
