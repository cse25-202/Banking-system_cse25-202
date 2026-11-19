package org.example;

import org.example.model.*;

public class Main {
    public static void main(String[] args) {
        Individual bogolo = new Individual("C001", "Bogolo", "Samoka", "Gaborone",
                "75775398", "samokab@mail.com", "bogolo", "pw7577");

        SavingsAccount sav = new SavingsAccount("S001", "Rail Park Mall", bogolo, 1000);
        InvestmentAccount inv = new InvestmentAccount("I001", "Rail Park Mall", bogolo, 1000);
        ChequeAccount chq = new ChequeAccount("Q001", "Rail Park Mall", bogolo, "BAC", "Gaborone");

        System.out.println("Savings withdraw allowed? " + sav.withdraw(100));
        chq.deposit(2000, "Salary");
        System.out.println("Cheque withdraw allowed? " + chq.withdraw(500));

        try {
            new InvestmentAccount("I002", "Main Mall", bogolo, 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        System.out.println("Accounts held: " + bogolo.getAccounts().size());

        for (Account a : bogolo.getAccounts()) {
            a.applyInterest();
            System.out.println(a.getAccountType() + " balance: " + a.getBalance());
        }
    }
}