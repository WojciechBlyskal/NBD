package org.example;

public class Guest
{
    String name;
    String lastName;
    String ID;
    String phoneNumber;

    public Guest(String name, String lastName, String ID, String phoneNumber) {
        this.name = name;
        this.lastName = lastName;
        this.ID = ID;
        this.phoneNumber = phoneNumber;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInfo() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("name", name)
                .append("lastName", lastName)
                .append("ID", ID)
                .append("phoneNumber", phoneNumber)
                .toString();
    }
}
