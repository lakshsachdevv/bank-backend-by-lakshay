package com.Sarvesh.Bank_System_backend.services;

import com.Sarvesh.Bank_System_backend.config.JwtTokenProvider;
import com.Sarvesh.Bank_System_backend.dto.*;
import com.Sarvesh.Bank_System_backend.entity.Role;
import com.Sarvesh.Bank_System_backend.entity.Transaction;
import com.Sarvesh.Bank_System_backend.entity.User;
import com.Sarvesh.Bank_System_backend.repository.UserRepository;
import com.Sarvesh.Bank_System_backend.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    EmailService emailService;
   @Autowired
    TranscationService transcationService;
   @Autowired
    PasswordEncoder passwordEncoder;
   @Autowired
    AuthenticationManager authenticationManager;
   @Autowired
    JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl() {

    }

    @Override
   public BankResponse debitAccount(creditdebitrequest request)
   {
       //check if Account exist
       boolean isAccountExist=userRepository.existsByAccountNumber(request.getAccountNumber());
       if(!isAccountExist) {
           return BankResponse.builder()
                   .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                   .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                   .accountInfo(null)
                   .build();
       }
       //check if amount you intend to withdraw is not more than account balance
       User usertoDebit = userRepository.findByAccountNumber(request.getAccountNumber());
       BigDecimal availablebalance = usertoDebit.getAccountBalance();
       BigDecimal debitAmount = request.getAmount();
       if(availablebalance.compareTo(debitAmount)<0)
       {
           return BankResponse.builder()
                   .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                   .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).
                   accountInfo(null)
           .build();
          }
       else {
           usertoDebit.setAccountBalance(availablebalance.subtract(debitAmount));
           userRepository.save(usertoDebit);
           Transactiondto transactiondto=Transactiondto.builder()
                   .accountNumber(usertoDebit.getAccountNumber())
                   .transactionType("Debit")
                   .amount(request.getAmount())
                   .build();
           transcationService.saveTransaction(transactiondto);
       }
       return  BankResponse.builder()
               .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
               .responseMessage(AccountUtils.ACCOUNT_DEBITED_SCUCCESS_MESSAGE)
               .accountInfo(AccountInfo.builder()
                       .accountNumber(request.getAccountNumber())
                       .accountName(usertoDebit.getFirstName()+" "+usertoDebit.getLastName())
                       .accountBalance(usertoDebit.getAccountBalance())
                       .build()).build();
   }

    @Override
    public BankResponse transfer(transferrequest request) {
       //get account to debit
        boolean isDestinationAccountExist=userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.DESTINATION_ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.DESTINATION_ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
   User sourceAccountUser=userRepository.findByAccountNumber(request.getSourceAccountNumber());
   if(sourceAccountUser.getAccountBalance().compareTo(request.getAmount())<0)
   {
       return BankResponse.builder()
               .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
               .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).
               accountInfo(null)
               .build();
   }
   sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
   String sourceUsername=sourceAccountUser.getFirstName()+sourceAccountUser.getLastName();
   userRepository.save(sourceAccountUser);
   EmailDetails debitAlert=EmailDetails.builder()
           .subject("Debit")
           .recipient(sourceAccountUser.getEmail())
           .messageBody("The sum of "+request.getAmount()+" has been deducted from your account! Your currenct account is "+sourceAccountUser.getAccountBalance())
           .build();
   emailService.setMailAlert(debitAlert);

   User destinationAccountUser=userRepository.findByAccountNumber(request.getDestinationAccountNumber());
   destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
   userRepository.save(destinationAccountUser);
        EmailDetails creditAlert=EmailDetails.builder()
                .subject("Credit")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of "+request.getAmount()+" has been sent to your account from "+sourceUsername+ " Your currenct account is "+destinationAccountUser.getAccountBalance())
                .build();
        emailService.setMailAlert(creditAlert);
        Transactiondto transactiondto=Transactiondto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("Credit")
                .amount(request.getAmount())
                .build();
        transcationService.saveTransaction(transactiondto);
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse creditAccount(creditdebitrequest request)
    {
        //checking if account exist
        boolean isAccountExist=userRepository.existsByAccountNumber(request.getAccountNumber());
        System.out.println(isAccountExist);
        if(!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User  usertoCredit=userRepository.findByAccountNumber(request.getAccountNumber());
         usertoCredit.setAccountBalance(usertoCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(usertoCredit);
        // save transaction
        Transactiondto transactiondto=Transactiondto.builder()
                .accountNumber(usertoCredit.getAccountNumber())
                .transactionType("Credit")
                .amount(request.getAmount())
                .build();
        transcationService.saveTransaction(transactiondto);
        return BankResponse.builder().
         responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                 .responseMessage(AccountUtils.ACCOUNT_CREDITED_SCUCCESS_MESSAGE)
                 .accountInfo(AccountInfo.builder()
                         .accountName(usertoCredit.getFirstName()+" "+usertoCredit.getLastName())
                 .accountBalance(usertoCredit.getAccountBalance())
                         .accountNumber(usertoCredit.getAccountNumber())
                         .build())

                 .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
      //check if provided account Number exixts in db
        boolean isAccountExist=userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist)
        {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser=userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse nameEnquiry(EnquiryRequest request) {
        //check if provided account Number exixts in db
        boolean isAccountExist=userRepository.existsByAccountNumber(request.getAccountNumber());
        System.out.println(isAccountExist);
        if(!isAccountExist)
        {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser=userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /*
          Creating an account - saving a new user into db
          check if user already has an account
         */
        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser= User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .state(userRequest.getState())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .status("Active")
                .role(Role.valueOf("ROLE_ADMIN".toString()))
                .build();
    User savedUser=userRepository.save(newUser);
    // Send Email Alert
        EmailDetails emailDetails=EmailDetails.builder().
                 recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congartulations! Your Account Has Been SuccessFully Created.\n Your Account Details:\n"+"Account Name: "+savedUser.getFirstName()+" "+savedUser.getLastName()+"\nAccount Number: "+savedUser.getAccountNumber())

                .build();
        emailService.setMailAlert(emailDetails);

    return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
            .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
            .accountInfo(AccountInfo.builder()
                    .accountBalance(savedUser.getAccountBalance())
                    .accountNumber(savedUser.getAccountNumber())
                    .accountName(savedUser.getFirstName()+" "+ savedUser.getLastName()).build())
            .build();
            }

     public  BankResponse login(Logindto logindto)
     {
         Authentication authentication=null;
         authentication=authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(logindto.getEmail(),logindto.getPassword())
         );
        EmailDetails loginAlert=EmailDetails.builder()
                .subject("You're logged in!")
                .recipient(logindto.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request,please contact your bank")
                .build();
        emailService.setMailAlert(loginAlert);
        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
     }

    @Override
    public BankResponse creditaccount(creditdebitrequest request) {
        return null;
    }

}
