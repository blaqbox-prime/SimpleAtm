package com.simpleatm.atm;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Transaction {
//    TODO: Common transaction Properties
    public String description;
    public LocalDateTime dateCreated;
    public double amount;
    public double balance;
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    Transaction(String desc, double amnt, double balance){
        description = desc;
        amount = amnt;
        dateCreated = LocalDateTime.now();
        this.balance = balance;
    }

    Transaction(String desc, double amnt, double balance, LocalDateTime dateTime){
        description = desc;
        amount = amnt;
        dateCreated = dateTime;
        this.balance = balance;
    }

    static Transaction fromString(String data){
        String[] transactionData = data.split(",");

        LocalDateTime dateTime = LocalDateTime.parse(transactionData[0], dateFormatter);
        String desc = transactionData[1];
        double amount = Double.parseDouble(transactionData[2]);
        double balance = Double.parseDouble(transactionData[3]);

        return new Transaction(desc,amount,balance,dateTime);
    }
    public String toString(){
        return dateCreated.format(dateFormatter) + "," + description + "," + amount + "," + balance;
    }

}
