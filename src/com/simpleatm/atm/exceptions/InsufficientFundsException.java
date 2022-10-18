package com.simpleatm.atm.exceptions;

import java.text.DecimalFormat;

public class InsufficientFundsException extends Exception{
       double availableBalance;
    DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");


    public InsufficientFundsException(double availableBalance){
        this.availableBalance = availableBalance;
    }

    @Override
    public String toString() {
        return "Insufficient Funds! available balance: " + moneyFormat.format(availableBalance);
    }
}
