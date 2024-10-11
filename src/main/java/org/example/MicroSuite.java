package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "microsuites")

public class MicroSuite extends Room {

    MicroSuite(long ID, int number, int floor, double surface, boolean balcony, double price, long version) {
        super(ID, number, floor, surface, balcony, price, version);
    }

    public MicroSuite() {

    }


    @Override
    public String getInfo() {
        return super.getInfo() + " This is a MicroSuite.";
    }
}
