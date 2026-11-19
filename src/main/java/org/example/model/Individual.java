package org.example.model;

public class Individual extends Customer {
    private String firstName;
    private String surname;
    private String phoneNumber;
    private String email;

    public Individual(String customerID, String firstName, String surname, String address,
                      String phoneNumber, String email, String username, String password) {
        super(customerID, address, username, password);
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }

    @Override
    public String getDisplayName() { return firstName + " " + surname; }
}