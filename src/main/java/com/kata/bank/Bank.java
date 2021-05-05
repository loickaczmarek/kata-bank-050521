package com.kata.bank;

import java.util.ArrayList;

public class Bank {

    public float balance;
    public ArrayList<String> transactions = new ArrayList<>();
    public TransfertService transfertService=new TransfertService();

    public float getBalance() {
        return balance;
    }

    public void deposit(float value, String date) {
        balance += value;
        transactions.add(date + " " + String.format("%.2f", value) + " " + String.format("%.2f", balance));
    }

    public void withdraw(float value, String date) {
        if(value <= balance) {
            balance -= value;
            transactions.add(date + " -" + String.format("%.2f", value) + " " + String.format("%.2f", balance));
        }
    }

    public ArrayList<String> listTransactions() {
        return transactions;
    }

    public void transfert(String ibanFrom, String ibanTo, int amount) {
        String code = transfertService.transfert(ibanFrom, ibanTo, amount);
        if ("202".equals(code)) {
            withdraw(amount, "");
        }
    }
}
