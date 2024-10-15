package org.example;

import jakarta.persistence.*;
import org.example.exception.GuestException;

import javax.annotation.processing.SupportedSourceVersion;
//import lombok.Getter;
//import lombok.Setter;

@Entity
@Table(name = "guests")
public class Guest {
    @Column
//    @Getter
    private String name;
    @Column
    private String lastName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column
    private String phoneNumber;
    @Version
    private long version;

    public Guest() {
    }

    public Guest(String name, String lastName, String phoneNumber) throws GuestException {
        if (name.isBlank()) {
            throw new GuestException("Name cannot be empty.");
        } else if (lastName.isBlank()) {
            throw new GuestException("Last name cannot be empty.");
        } else if (phoneNumber.isBlank()) {
            throw new GuestException("Phone number cannot be empty.");
        } else {
            this.name = name;
            this.lastName = lastName;
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
        return Id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInfo() {
        return "First name: " + getName() + ". Last name is: " + getLastName() + ". ID is: " + getId()
                + "Phone number is: " + getPhoneNumber() + ".";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Id == guest.Id
                && name.equals(guest.name)
                && lastName.equals(guest.lastName)
                && phoneNumber.equals(guest.phoneNumber);
    }
}