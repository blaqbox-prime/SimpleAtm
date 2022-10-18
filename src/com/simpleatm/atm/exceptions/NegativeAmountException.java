package com.simpleatm.atm.exceptions;

public class NegativeAmountException extends Exception {
    public NegativeAmountException(){}

    @Override
    public String toString() {
        return "Entered amount cannot be negative";
    }
}
