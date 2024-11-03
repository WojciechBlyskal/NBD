package org.example.model;

import org.example.exception.GuestException;

public class Guest
{
    private String name;
    private String lastName;
    private long id;
    private String phoneNumber;

    public Guest(String name, String lastName, long id, String phoneNumber) throws GuestException {
        if (name.isBlank()) {
            throw new GuestException("Name cannot be empty.");
        } else if (lastName.isBlank()) {
            throw new GuestException("Last name cannot be empty.");
        } else if (phoneNumber.isBlank()) {
            throw new GuestException("Phone number cannot be empty.");
        } else {
            this.name = name;
            this.lastName = lastName;
            this.id = id;
            this.phoneNumber = phoneNumber;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long Id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInfo() {
        return "First name: " + getName() + ". Last name is: " + getLastName() + ". ID is: " + String.valueOf(getId())
                + "Phone number is: " + getPhoneNumber() + ".";
    }
}