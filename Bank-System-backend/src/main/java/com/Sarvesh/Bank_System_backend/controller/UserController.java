package com.Sarvesh.Bank_System_backend.controller;

import com.Sarvesh.Bank_System_backend.dto.*;
import com.Sarvesh.Bank_System_backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name="User Account Management Apis")
public class UserController {
    @Autowired
    UserService userService;
    @Operation(
            summary = "Create new User Account",
            description = "Creating a new User and assigning an account ID"
    )
    @ApiResponse(
            responseCode ="201",
            description = "Http status 201 created"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest)
    {
        return userService.createAccount(userRequest);
    }
    @PostMapping("/login")
    public BankResponse login(@RequestBody Logindto logindto)
    {
        return userService.login(logindto);
    }
    @Operation(
            summary = "Balance Enquiry",
            description = "Check about the balance of user from his account number"
    )
    @ApiResponse(
            responseCode ="200",
            description = "Http status 200 Success"
    )
    @PostMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request)
    {
        return userService.balanceEnquiry(request);
    }

    @GetMapping("/nameEnquiry")
        public BankResponse nameEnquiry(@RequestBody EnquiryRequest request)
        {
            return userService.nameEnquiry(request);
        }
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody creditdebitrequest request)
    {
        return userService.creditAccount(request);
    }
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody creditdebitrequest request)
    {
        return userService.debitAccount(request);
    }
    @PostMapping("/transfer")
    public  BankResponse transfer(@RequestBody transferrequest request)
    {
        return userService.transfer(request);
    }
}

