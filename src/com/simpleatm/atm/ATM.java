package com.simpleatm.atm;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;


public class ATM {


    public static void main(String[] args) throws java.io.IOException {
//        Login Tries Available

//        Opening Banner
        System.out.println("------------------------------------");
        System.out.println("Welcome To ACME Bank");
        System.out.println("------------------------------------");

        Menu.start();

//        Closing Banner
        System.out.println("------------------------------------");
        System.out.println("Goodbye");
        System.out.println("------------------------------------");


    }

}
