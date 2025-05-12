package com.Sarvesh.Bank_System_backend.services;

import com.Sarvesh.Bank_System_backend.dto.EmailDetails;
import com.Sarvesh.Bank_System_backend.entity.Transaction;
import com.Sarvesh.Bank_System_backend.entity.User;
import com.Sarvesh.Bank_System_backend.repository.TransactionRepository;
import com.Sarvesh.Bank_System_backend.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.PageSize;


@Component
@AllArgsConstructor
public class BankStatement {
    /*
    retrieve list of transaction within a date range given an account number
    generate a pdf file of transaction
    send file via email
     */

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private static  final String FILE="D:\\Bank_project\\Mystatement.pdf";
    private static final Logger log = (Logger) LoggerFactory.getLogger(BankStatement.class);

    public List<Transaction> generateStatement(String accountNumber,String startDate,String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start= LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> list= transactionRepository.findAll().stream()
                .filter(transactions -> transactions.getAccountNumber().equals(accountNumber))
                .filter(transactions -> {
                    LocalDate transactionDate = transactions.getCreatedAt().toLocalDate();  // Assuming getCreatedAt() returns LocalDateTime
                    return (transactionDate.isEqual(start) || transactionDate.isAfter(start)) &&
                            (transactionDate.isEqual(end) || transactionDate.isBefore(end));
                })
                .toList();

        User user=userRepository.findByAccountNumber(accountNumber);
        String coustomerName= user.getFirstName()+" "+user.getLastName();
        Rectangle statementSize=new Rectangle(PageSize.A4);
        Document document=new Document(statementSize);
        log.info("Setting size of Document");
        OutputStream outputStream=new FileOutputStream(FILE);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        PdfPTable bankInfoTable= new PdfPTable(1);
        PdfPCell bankName=new PdfPCell(new Phrase("The ShivRam Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress=new PdfPCell(new Phrase("V/p Sikri Haliyapur Sultanpur"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo=new PdfPTable(2);
        PdfPCell customerInfo=new PdfPCell(new Phrase("Start Date :"+ startDate));
        customerInfo.setBorder(0);
        PdfPCell statement =new PdfPCell(new Phrase("Statement ofAccount"));
        statement.setBorder(0);
        PdfPCell stopDate=new PdfPCell(new Phrase("End Date : "+ endDate));
        stopDate.setBorder(0);
        PdfPCell name=new PdfPCell(new Phrase("Coustomer Name : "+coustomerName));
        name.setBorder(0);
        PdfPCell space=new PdfPCell();
        space.setBorder(0);
        PdfPCell address=new PdfPCell(new Phrase("Coustomer Address : "+user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable=new PdfPTable(4);
        PdfPCell date=new PdfPCell(new Phrase("Date"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType =new PdfPCell(new Phrase("Transaction Type"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmount=new PdfPCell(new Phrase("Transaction Amount"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);
        PdfPCell status=new PdfPCell(new Phrase("Status"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        list.forEach(transaction -> {
            transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);
        document.close();

        EmailDetails emailDetails=EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Account Statement")
                .messageBody("Kindly find your requested account statement attached")
                .attachment(FILE)
                .build();
          emailService.sendEmailwithAttachment(emailDetails);

        return list;
    }

}
