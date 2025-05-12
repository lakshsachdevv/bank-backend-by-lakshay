package com.Sarvesh.Bank_System_backend.utils;

import java.time.Year;

public class AccountUtils {

  public static final String ACCOUNT_EXISTS_CODE="001";

  public static final String ACCOUNT_EXISTS_MESSAGE="This user already has an account Created!";

  public  static final String ACCOUNT_CREATION_SUCCESS="002";

  public static final String ACCOUNT_CREATION_MESSAGE="Account has been succesfully created!";
  public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
  public static final String ACCOUNT_NOT_EXIST_MESSAGE="User with provide Account number doesn't exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE="User with provide Account number Founded";
    public  static final String ACCOUNT_CREDITED_SUCCESS="005";
    public static final String ACCOUNT_CREDITED_SCUCCESS_MESSAGE="Account has been succesfully credited!";
    public  static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE="Account does not have sufficient balance!";
    public static final String ACCOUNT_DEBITED_SUCCESS ="007" ;
    public static final String ACCOUNT_DEBITED_SCUCCESS_MESSAGE="Account has been succesfully credited!";
    public static final String SOURCE_ACCOUNT_NOT_EXISTS_CODE = "008";
    public static final String SOURCE_ACCOUNT_NOT_EXIST_MESSAGE="SourceUser with provide Account number doesn't exist";
    public static final String DESTINATION_ACCOUNT_NOT_EXISTS_CODE = "009";
    public static final String DESTINATION_ACCOUNT_NOT_EXIST_MESSAGE="DestinationUser with provide Account number doesn't exist";
    public static final String TRANSFER_SUCCESS_CODE = "010";
    public static final String TRANSFER_SUCCESS_MESSAGE="Transfer Successfull";
    public static String generateAccountNumber()
    {
        /**
         * 2025 + randomsixdigits
         */
        Year currentYear= Year.now();
        int min=100000;
        int max=999999;

        // generate a random number between min and max
        int randNumber= (int) Math.floor(Math.random()*(max-min +1)+min);

        //convert the currentYear and random number to String, then concatenate
        String year=String.valueOf(currentYear);

        String randomnumber=String.valueOf(randNumber);
        StringBuilder accountNumber=new StringBuilder();
        accountNumber.append(year).append(randomnumber);
         return accountNumber.toString();
    }
}
