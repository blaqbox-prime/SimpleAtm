package com.simpleatm.atm;

import com.simpleatm.atm.exceptions.InsufficientFundsException;
import com.simpleatm.atm.exceptions.NegativeAmountException;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Account {
    private final int accountNum;
    private int pin;
    private double savingsBalance;
    private double chequeBalance;



    Scanner sc = new Scanner(System.in);
    DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String OUTPUT_FORMAT = "|%16s|%12s|%15s|%15s|";
    String HEADINGS = String.format(OUTPUT_FORMAT,"Date","Description","Amount","Balance");
    String ROW_SEPARATOR = String.format(OUTPUT_FORMAT,"----------------","------------","---------------","---------------");


    Account(int accNum, int pinNum, double initialSavingsBalance, double initialChequeBalance ){
        this.accountNum = accNum;
        this.pin = pinNum;
        this.savingsBalance = initialSavingsBalance;
        this.chequeBalance = initialChequeBalance;
    };

    public int getAccountNum() {
        return accountNum;
    }

    public int getPin() {
        return pin;
    }

    public double getChequeBalance() {
        return chequeBalance;
    }

    public double getSavingsBalance() {
        return savingsBalance;
    }

    public void depositToSavings(){

        double amount = 0.0;
        System.out.println("Current balance - " + moneyFormat.format(savingsBalance));
        System.out.print("Enter deposit amount: ");
        try {
            amount = sc.nextDouble();
            if(amount < 0) amount = 0.0; //all negative number inputs default to zero
            savingsBalance += amount;
            Transaction tr = new Transaction("deposit",amount,savingsBalance);
            Services.recordNewTransaction(this,tr,"savings");
            System.out.println("New balance - " + moneyFormat.format(savingsBalance));
        } catch (InputMismatchException e){
            System.out.println("Invalid amount");
        }
    }

    public void depositToChequeing(){

        double amount = 0.0;
        System.out.println("Current balance - " + moneyFormat.format(chequeBalance));
        System.out.print("Enter deposit amount: ");
        try {
            amount = sc.nextDouble();
            if(amount < 0) amount = 0.0; //all negative number inputs default to zero
            chequeBalance += amount;
            Transaction tr = new Transaction("deposit",amount,chequeBalance);
            Services.recordNewTransaction(this,tr,"cheque");
            System.out.println("New balance - " + moneyFormat.format(chequeBalance));
        } catch (InputMismatchException e){
            System.out.println("Invalid amount");
        }
    }

    public void withdrawFromSavings(){

        double amount = 0.0;
        System.out.println("Current balance - " + moneyFormat.format(savingsBalance));
        System.out.print("Enter withdrawal amount: ");
        try {
            amount = sc.nextDouble();
            if(amount > savingsBalance) throw new InsufficientFundsException(savingsBalance);
            savingsBalance -= amount;
            Transaction tr = new Transaction("withdraw",amount,savingsBalance);
            Services.recordNewTransaction(this,tr,"savings");
            System.out.println("New balance - " + moneyFormat.format(savingsBalance));
        } catch (InputMismatchException e){
            System.out.println("Invalid amount");
        }catch (InsufficientFundsException e){
            System.out.println(e.toString());
        }
    }

    public void withdrawFromCheque(){

        double amount = 0.0;
        System.out.println("Current balance - " + moneyFormat.format(chequeBalance));
        System.out.print("Enter withdrawal amount: ");
        try {
            amount = sc.nextDouble();
            if(amount > savingsBalance) throw new InsufficientFundsException(chequeBalance);
            savingsBalance -= amount;
            Transaction tr = new Transaction("withdraw",amount,chequeBalance);
            Services.recordNewTransaction(this,tr,"cheque");
            System.out.println("New balance - " + moneyFormat.format(chequeBalance));
        } catch (InputMismatchException e){
            System.out.println("Invalid amount");
        }catch (InsufficientFundsException e){
            System.out.println(e.toString());
        }
    }

    public void transferFromSavings(){
            boolean end = false;

            do {
                double amount = 0.0;
                System.out.println("Current balance - " + moneyFormat.format(savingsBalance));
                System.out.println("Choose account to transfer to: ");
                System.out.println("\t1. Cheque");
                System.out.println("\t2. Exit");
                System.out.print("\tSelect an option: ");

                try {
                    int choice = sc.nextInt();

                    switch (choice){
                        case 1 -> {
                            System.out.print("Enter amount to transfer: ");
                            amount = sc.nextDouble();
                            if(amount < 0) throw new NegativeAmountException();
                            if(amount > savingsBalance) throw new InsufficientFundsException(savingsBalance);
                            savingsBalance -= amount;
                            chequeBalance += amount;

                            //Record transfer into Savings statement
                            Transaction tr = new Transaction("transfer",amount,savingsBalance);
                            Services.recordNewTransaction(this,tr,"savings");

                            //Record transfer into Cheque statement
                            Transaction trCheque = new Transaction("transfer",amount,chequeBalance);
                            Services.recordNewTransaction(this,trCheque,"cheque");

                            System.out.println("New balance - " + moneyFormat.format(savingsBalance));
                            end = true;
                        }
                        case 2 -> {
                            return;
                        }
                        default -> {
                            System.out.println("Invalid selection");
                        }
                    }
                } catch (InputMismatchException e){
                    System.out.println("Invalid amount");
                }catch (InsufficientFundsException e){
                    System.out.println(e.toString());
                }catch (NegativeAmountException e){
                    System.out.println(e.toString());
                }
            } while (!end);
        }

    public void transferFromCheque(){
        boolean end = false;

        do {
            double amount = 0.0;
            System.out.println("Current balance - " + moneyFormat.format(chequeBalance));
            System.out.println("Choose account to transfer to: ");
            System.out.println("\t1. Savings");
            System.out.println("\t2. Exit");
            System.out.print("\tSelect an option: ");

            try {
                int choice = sc.nextInt();

                switch (choice){
                    case 1 -> {
                        System.out.print("Enter amount to transfer: ");
                        amount = sc.nextDouble();
                        if(amount < 0) throw new NegativeAmountException();
                        if(amount > chequeBalance) throw new InsufficientFundsException(chequeBalance);
                        chequeBalance -= amount;
                        savingsBalance += amount;

                        //Record transfer into cheque statement
                        Transaction tr = new Transaction("transfer",amount,chequeBalance);
                        Services.recordNewTransaction(this,tr,"cheque");

                        //Record transfer into Savings statement
                        Transaction trSavings = new Transaction("transfer",amount,savingsBalance);
                        Services.recordNewTransaction(this,trSavings,"savings");

                        System.out.println("New balance - " + moneyFormat.format(chequeBalance));
                        end = true;
                    }
                    case 2 -> {
                        return;
                    }
                    default -> {
                        System.out.println("Invalid selection");
                    }
                }
            } catch (InputMismatchException e){
                System.out.println("Invalid Characters: Only numbers allowed");
            }catch (InsufficientFundsException e){
                System.out.println(e.toString());
            }catch (NegativeAmountException e){
                System.out.println(e.toString());
            }
        } while (!end);
    }

    void showSavingsTransactionStatement(){
        try{
            String filename = "statements/"+getAccountNum()+"-savings.txt";
            ArrayList<Transaction> transactions = Services.getTransactionsStatement(filename);
          printTransactions(transactions);
        }catch (Exception e){
            System.out.println("No statement found.");
        }

    }

    void showChequeTransactionStatement(){

        try{
            String filename = "statements/"+getAccountNum()+"-cheque.txt";
            ArrayList<Transaction> transactions = Services.getTransactionsStatement(filename);

            printTransactions(transactions);
        }catch (Exception e){
            System.out.println("No statement found");
        }

    }

    private void printTransactions(ArrayList<Transaction> transactions) {
        if(transactions.size() > 0){
            System.out.println(ROW_SEPARATOR);
            System.out.println(HEADINGS);
            System.out.println(ROW_SEPARATOR);

            for (int i = 0; i < transactions.size() ; i++) {
                Transaction tr = transactions.get(i);
                System.out.println(String.format(OUTPUT_FORMAT,tr.dateCreated.format(dateFormatter), tr.description,moneyFormat.format(tr.amount),moneyFormat.format(tr.balance)));
            }
            System.out.println(ROW_SEPARATOR);

        }else {
            System.out.println("No transaction history");
        }
    }

    @Override
    public String toString() {
        return accountNum + "," + savingsBalance + "," + chequeBalance;
    }

    static Account fromString(String accountString){
        double savingBalance, chequeBalance;
        int accNum, pin;


        String[] data = accountString.split(",");

        //read
        accNum = Integer.parseInt(data[0]);
        savingBalance = Double.parseDouble(data[1]);
        chequeBalance = Double.parseDouble(data[2]);
        pin = Integer.parseInt(data[3]);

        return new Account(accNum,pin,savingBalance,chequeBalance);

    }

}


