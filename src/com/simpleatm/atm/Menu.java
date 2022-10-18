package com.simpleatm.atm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Menu {
    static Account loggedInAccount;

    static HashMap<Integer,Account> accountsList = Services.loadRegisteredAccounts();

    static Scanner sc = new Scanner(System.in);

    static DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");


    static void saveChanges(){
        accountsList.put(loggedInAccount.getAccountNum(),loggedInAccount);
        try(FileWriter fw = new FileWriter("Accounts.txt");
            BufferedWriter bw = new BufferedWriter(fw)
        ){
            //build comma separated string
            StringBuffer stringBuffer = new StringBuffer("");
            Iterator it = accountsList.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                Account acc = (Account) pair.getValue();
                stringBuffer.append(acc.toString()).append(",").append(acc.getPin()).append('\n');
                bw.write(stringBuffer.toString());
            }

        }catch(IOException e){
            System.out.println(e.toString());
        }catch(Exception e){
            System.out.println("Failed to save changes.");
        }
    }
    static void login() {
        boolean end = false;
        int inputAccNum;
        int inputPin;
        int attempts = 0;

        do {
            attempts++;
            System.out.print("Enter your account number: ");
            inputAccNum = sc.nextInt();
            System.out.print("Enter your pin: ");
            inputPin = sc.nextInt();
            System.out.println();

            Iterator it = accountsList.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                Account acc = (Account) pair.getValue();
                if (accountsList.containsKey(inputAccNum) && inputPin == acc.getPin()) {
                    loggedInAccount = acc;
                    accountSelectMenu();
                    end = true;
                    break;
                }
            }
            if (!end) {
                System.out.println("Wrong Account Number or Pin");
            }

            if(attempts == 3) end = true;

        } while (!end);
    }

    static void accountSelectMenu(){
        boolean end = false;

        do {
            System.out.println("Select an account:");
            System.out.println("\t1. Savings Account");
            System.out.println("\t2. Cheque Account");
            System.out.println("\t3. Exit");

            System.out.print("Choice: ");
            int choice = sc.nextInt();
            System.out.println();

            switch (choice) {
                case 1 -> showSavingsMenu();
                case 2 -> showChequeMenu();
                case 3 -> end = true;
            }
        }while(!end);

    }

    static void showSavingsMenu(){
        boolean end = false;
        do {
            System.out.println("------------------------------");
            System.out.println("Savings Account");
            System.out.println("------------------------------");
            System.out.println("");

            System.out.println("\t1. Balance");
            System.out.println("\t2. Deposit");
            System.out.println("\t3. Withdraw");
            System.out.println("\t4. Transfer");
            System.out.println("\t5. Statement");
            System.out.println("\t6. Exit");

            System.out.println();
            System.out.print("\tSelect an option: ");

            int choice = sc.nextInt();
            switch (choice){
                case 1 -> System.out.println("Savings account balance " + moneyFormat.format(loggedInAccount.getSavingsBalance()));
                case 2 -> loggedInAccount.depositToSavings();
                case 3 -> loggedInAccount.withdrawFromSavings();
                case 4 -> loggedInAccount.transferFromSavings();
                case 5 -> loggedInAccount.showSavingsTransactionStatement();
                default -> end = true;
            }

        } while (!end);
    }
    
    static void showChequeMenu(){
        boolean end = false;
        do {
            System.out.println("------------------------------");
            System.out.println("Cheque account");
            System.out.println("------------------------------");
            System.out.println("");

            System.out.println("\t1. Balance");
            System.out.println("\t2. Deposit");
            System.out.println("\t3. Withdraw");
            System.out.println("\t4. Transfer");
            System.out.println("\t5. Statement");
            System.out.println("\t6. Exit");

            System.out.println();
            System.out.print("\tSelect an option: ");

            int choice = sc.nextInt();
            switch (choice){
                case 1 -> System.out.println("Cheque account balance " + moneyFormat.format(loggedInAccount.getChequeBalance()));
                case 2 -> loggedInAccount.depositToChequeing();
                case 3 -> loggedInAccount.withdrawFromCheque();
                case 4 -> loggedInAccount.transferFromCheque();
                case 5 -> loggedInAccount.showChequeTransactionStatement();
                default -> end = true;
            }

        } while (!end);
    }

    static void start(){
        boolean end = false;

        do {
            System.out.println("\t1. Login");
            System.out.println("\t2. Create Account");
            System.out.println("\t3. Exit");
            System.out.println();
            System.out.print("\tSelect an option: ");


            int choice = sc.nextInt();

            switch (choice){
                case 1 -> {
                    login();
                    return;
                }
                case 2 -> {
                    createAccount();
                    return;
                }
                case 3 -> {
                    end = true;
                }
            }
        }while(!end);
    }

    static void createAccount(){
        int accNum = Services.generateNewAccountNum();
        int pin;
        double initialSavings = 0.0;
        double initialCheque = 0.0;

        boolean end = false;

        System.out.println("------------------------------");
        System.out.println("Create new account");
        System.out.println("------------------------------");
        System.out.println("\t Your Account Number is: " + accNum);

        do {
            try {
                System.out.print("Create new pin: ");
                pin = sc.nextInt();
                System.out.println();
                System.out.print("Enter initial savings deposit: ");
                initialSavings = sc.nextDouble();
                System.out.println();
                System.out.print("Enter initial checking deposit: ");
                initialCheque = sc.nextDouble();

                Account newAcc = new Account(accNum,pin,initialSavings,initialCheque);
                Services.recordNewAccount(newAcc);
                accountsList = Services.loadRegisteredAccounts();

                end = true;

            }catch (InputMismatchException e){
                System.out.println("Invalid Input! Only numbers allowed");
                end = true;
            }
        }while (!end);

        System.out.println("----- Redirecting to Login ------");
        login();

    }


}
