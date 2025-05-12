package com.Sarvesh.Bank_System_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logindto {
    private String email;
    private String  password;
}
