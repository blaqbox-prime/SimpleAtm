package com.simpleatm.atm.exceptions;

public class InsufficientFunds extends Exception{
       double availableBalance;

    InsufficientFunds(double availableBalance){
        this.availableBalance = availableBalance;
    }

    @Override
    public String toString() {
        return "Insufficient Funds! avaialable balance: " + availableBalance;
    }
}
