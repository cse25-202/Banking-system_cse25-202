package org.example.model;

public class Company extends Customer {
    private String companyName;
    private String registrationName;
    private String companyEmail;
    private String telephoneNumber;

    public Company(String customerID, String companyName, String registrationName, String address,
                   String companyEmail, String telephoneNumber, String username, String password) {
        super(customerID, address, username, password);
        this.companyName = companyName;
        this.registrationName = registrationName;
        this.companyEmail = companyEmail;
        this.telephoneNumber = telephoneNumber;
    }

    public String getCompanyName() { return companyName; }
    public String getRegistrationName() { return registrationName; }
    public String getCompanyEmail() { return companyEmail; }
    public String getTelephoneNumber() { return telephoneNumber; }

    @Override
    public String getDisplayName() { return companyName; }
}