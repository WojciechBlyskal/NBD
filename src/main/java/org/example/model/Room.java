package org.example.model;

import org.example.exception.RoomException;

public abstract class Room {
    private int number;
    private int floor;
    private double surface;
    private boolean balcony;
    private double price;

    public Room(int number, int floor, double surface, boolean balcony, double price) {
        if (number < 1) {
            throw new RoomException("Room number cannot be lower than 1.");
        } else if (surface <= 0) {
            throw new RoomException("Room surface cannot be 0 or lower.");
        } else if (price < 0) {
            throw new RoomException("Room price cannot be lower than 0.");
        } else {
            this.number = number;
            this.floor = floor;
            this.surface = surface;
            this.balcony = balcony;
            this.price = price;
        }
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
        return "Room number " + String.valueOf(getNumber()) + " on floor " + String.valueOf(getFloor())
                + (isBalcony()?" with balcony.":" without balcony.") + " It has the surface of "
                + String.valueOf(getSurface()) + ". Price per day equals " + String.valueOf(getPrice()) + ".";

    }
}

