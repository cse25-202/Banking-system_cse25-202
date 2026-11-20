package org.example.dao;

import org.example.model.*;

public class DatabaseSeeder {
    private static final String BRANCH = "Rail Park Mall";

    public static void seedIfEmpty() {
        CustomerDAO customerDAO = new CustomerDAO();
        if (customerDAO.count() > 0) return;
        AccountDAO accountDAO = new AccountDAO();

        String[][] people = {
                {"C001", "Bogolo", "Samoka", "Gaborone", "75775398", "samokab@mail.com", "bogolo", "pw7577"},
                {"C002", "Thabo", "Moeng", "Francistown", "71098771", "thabo@mail.com", "thabo", "pw1234"},
                {"C003", "Chantell", "Kgosi", "Maun", "73456802", "neo@mail.com", "chantell", "pw4231"},
                {"C004", "Kane", "Dube", "Palapye", "75113489", "kabelo@mail.com", "kane", "pw3489"},
                {"C005", "Lesego", "Mokoena", "Serowe", "78764531", "lesego@mail.com", "lesego", "pw4531"},
                {"C006", "Patrick", "Sebego", "Maun", "75903456", "tumelo@mail.com", "patrick", "pw3456"},
                {"C007", "Ogone", "Ntsima", "Kanye", "76340063", "mpho@mail.com", "ogone", "pw0063"},
                {"C008", "Boikanyo", "Phil", "Molepolole", "71709782", "boikanyo@mail.com", "boitumelo", "pw9782"},
                {"C009", "Kagiso", "Tau", "Mahalapye", "78378134", "kagiso@mail.com", "kagiso", "pw8134"},
        };

        for (int i = 0; i < people.length; i++) {
            String[] p = people[i];
            Individual customer = new Individual(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
            customerDAO.insert(customer);

            accountDAO.save(new SavingsAccount("S" + (1001 + i), BRANCH, customer, 1000 + i * 250));
            if (i % 2 == 0) {
                accountDAO.save(new InvestmentAccount("I" + (1001 + i), BRANCH, customer, 1000 + i * 100));
            }
            if (i % 3 == 0) {
                ChequeAccount cheque = new ChequeAccount("Q" + (1001 + i), BRANCH, customer, "BAC", "Gaborone");
                cheque.deposit(5000, "Salary");
                accountDAO.save(cheque);
            }
        }

        Company company = new Company("C010", "Kalahari Traders (Pty) Ltd", "BW00012345",
                "Plot 123, Gaborone", "info@kalahari.co.bw", "3900000", "kalahari", "pw1234");
        customerDAO.insert(company);
        accountDAO.save(new SavingsAccount("S1010", BRANCH, company, 5000));
        accountDAO.save(new InvestmentAccount("I1010", BRANCH, company, 10000));
    }
}