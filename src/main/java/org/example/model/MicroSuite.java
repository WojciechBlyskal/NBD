package org.example.model;

public class MicroSuite extends Room {
    MicroSuite(int number, int floor, double surface, boolean balcony, double price) {
        super(number, floor, surface, balcony, price);
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " This is a MicroSuite.";
    }
}
