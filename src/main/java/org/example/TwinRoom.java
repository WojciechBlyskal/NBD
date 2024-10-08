package org.example;

public class TwinRoom extends MultipleRoom {
    public TwinRoom(int number, int floor, double surface, boolean balcony, double price, int forHowManyPeople) {
        super(number, floor, surface, balcony, price, forHowManyPeople);
    }
    public String getInfo() {
        return super.getInfo() + "This is a Twin Room.";
    }
}
