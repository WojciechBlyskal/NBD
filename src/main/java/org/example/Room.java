package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Room {
    int number;
    int floor;
    double surface;
    boolean balcony;
    double price;

    public Room(int number, int floor, double surface, boolean balcony, double price) {
        this.number = number;
        this.floor = floor;
        this.surface = surface;
        this.balcony = balcony;
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public boolean isBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInfo() {
        return new ToStringBuilder(this)
                .append("number", number)
                .append("floor", floor)
                .append("surface", surface)
                .append("balcony", balcony)
                .append("price", price)
                .toString();
    }
}

