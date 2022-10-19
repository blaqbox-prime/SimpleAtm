package com.simpleatm.atm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Menu {
    static Account loggedInAccount;

    static HashMap<Integer,Account> accountsList;

    static Scanner sc = new Scanner(System.in);

    static DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");


    static void saveChanges(){
        accountsList.put(loggedInAccount.getAccountNum(),loggedInAccount);
        try(FileWriter fw = new FileWriter("Accounts.txt");
            BufferedWriter bw = new BufferedWriter(fw)
        ){
            //build comma separated string
            StringBuilder stringBuffer = new StringBuilder();
            for (Map.Entry<Integer, Account> integerAccountEntry : accountsList.entrySet()) {
                Account acc = integerAccountEntry.getValue();
                stringBuffer.append(acc.toString()).append(",").append(acc.getPin()).append('\n');
                bw.write(stringBuffer.toString());
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println("Failed to save changes.");
        }
    }
    static void login() {
        accountsList = Services.loadRegisteredAccounts();
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

            for (Map.Entry<Integer, Account> integerAccountEntry : accountsList.entrySet()) {
                Account acc = (Account) ((Map.Entry<?, ?>) integerAccountEntry).getValue();
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
        saveChanges();

    }

    static void showSavingsMenu(){
        boolean end = false;
        do {
            System.out.println("------------------------------");
            System.out.println("Savings Account");
            System.out.println("------------------------------");
            System.out.println();

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
            System.out.println();

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
                case 3 -> end = true;
            }
        }while(!end);
    }

    static void createAccount(){
        int accNum = Services.generateNewAccountNum();
        int pin = 12502;
        double initialSavings = 0.0;
        double initialCheque = 0.0;


        System.out.println("------------------------------");
        System.out.println("Create new account");
        System.out.println("------------------------------");
        System.out.println("\t Your Account Number is: " + accNum);

        boolean validPin = false, validSavingsDep = false, validChequeDep = false;

        do {
            // get pin
            try{
                System.out.print("Create new pin: ");
                pin = sc.nextInt();
                System.out.println();
                validPin = true;
            }catch (Exception e){
                System.out.println("Pin must be digits only");
                sc.nextLine();
            }
        }while(!validPin);

        // get initial savings deposit
        do {
            try {
                System.out.print("Enter initial savings deposit: ");
                initialSavings = sc.nextDouble();
                System.out.println();
                validSavingsDep = true;
            }catch (Exception e){
                System.out.println("Invalid input: Separate decimals by a comma.");
                sc.nextLine();
            }
        }while(!validSavingsDep);

        //get initial Cheque deposit
        do {
            try {
                System.out.print("Enter initial checking deposit: ");
                initialCheque = sc.nextDouble();
                validChequeDep = true;
            }catch (Exception e){
                System.out.println("Invalid input: Separate decimals by a comma.");
                sc.nextLine();
            }
        }while(!validChequeDep);

        //create Account

        Account newAcc = new Account(accNum,pin,initialSavings,initialCheque);
        Services.recordNewAccount(newAcc);

        System.out.println("----- Redirecting to Login ------");
        login();

    }


}
