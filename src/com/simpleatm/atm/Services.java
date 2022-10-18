package com.simpleatm.atm;




import java.io.*;


import java.util.*;

public class Services {

    static int generateNewAccountNum(){
        int num = (int) Math.floor(Math.random() * 1000000000);
        return num;
    }


    static void recordNewAccount(Account acc){

        try(FileWriter fw = new FileWriter("Accounts.txt",true);
            BufferedWriter bw = new BufferedWriter(fw)
        ){
            //build comma separated string
            StringBuffer stringBuffer = new StringBuffer("");
            //encrypt pin
            String pin = String.valueOf(acc.getPin());


            stringBuffer.append(acc.toString()).append(",").append(acc.getPin()).append('\n');
            bw.write(stringBuffer.toString());

        }catch(IOException e){
            System.out.println(e.toString());
        }catch(Exception e){
            System.out.println("Failed to create account.");
        }
    }

    static void recordNewTransaction(Account acc, Transaction transaction, String accType){
        String filename = "statements/"+acc.getAccountNum()+"-"+accType+".txt";
        try(FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
        ){
            bw.write(transaction.toString());
            bw.append('\n');

        }catch(IOException e){
            System.out.println(e.toString());
        }catch(Exception e){
            System.out.println("Failed to record transaction.");
        }
    }

    static HashMap<Integer,Account> loadRegisteredAccounts(){
        HashMap<Integer,Account> registeredAccounts = new HashMap<>();

        try (FileReader fr = new FileReader("Accounts.txt");
             BufferedReader reader = new BufferedReader(fr)
        ){
            int i;
            String accData = reader.readLine();
            if(accData == null){
                return registeredAccounts;
            }
            while(accData != null) {
                    Account acc = Account.fromString(accData);
                    registeredAccounts.put(acc.getAccountNum(),acc);
                    accData = reader.readLine();
            }


        }catch (IOException e){
            System.out.println(e.toString());
        }
        return registeredAccounts;
    }

    static ArrayList<Transaction> getTransactionsStatement(String filename) throws IOException{
        ArrayList<Transaction> transactions = new ArrayList<>();

        try (FileReader fr = new FileReader(filename);
             BufferedReader reader = new BufferedReader(fr)
        ){
            int i;

            String transactionData = reader.readLine();

            while(transactionData != null) {
                Transaction tr = Transaction.fromString(transactionData);
                transactions.add(tr);
                transactionData = reader.readLine();
            }


        }catch (IOException e){
            System.out.println(e.toString());
            throw e;
        }
        return transactions;
    }


}
