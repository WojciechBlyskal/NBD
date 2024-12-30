package org.example.model;

import org.example.exception.GuestException;
import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(defaultKeyspace = "site")
@CqlName("guests")
public class Guest {
    @PartitionKey
    private UUID id;

    @CqlName("name")
    private String name;

    @CqlName("lastname")
    private String lastName;

    @CqlName("phoneNumber")
    private String phoneNumber;

    @CqlName("rent_ids")
    private Set<UUID> rentIds = new HashSet<>();

    public Guest() {
    }

    public Guest(String name, String lastName, String phoneNumber, UUID id, Set<UUID> rentIds) throws GuestException {
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
            this.id = id;
            this.rentIds = rentIds;
        }
    }

    public Guest(String name, String lastName, String phoneNumber) throws GuestException {
        if (name.isBlank()) {
            throw new GuestException("Name cannot be empty.");
        } else if (lastName.isBlank()) {
            throw new GuestException("Last name cannot be empty.");
        } else if (phoneNumber.isBlank()) {
            throw new GuestException("Phone number cannot be empty.");
        } else {
            this.id = UUID.randomUUID();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<UUID> getRentIds() {
        return rentIds;
    }

    public void setRentIds(Set<UUID> rentIds) {
        this.rentIds = rentIds;
    }
}