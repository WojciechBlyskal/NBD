package org.example;

public class FamilyRoom extends MultipleRoom {
    public FamilyRoom(int number, int floor, double surface, boolean balcony, double price, int forHowManyPeople) {
        super(number, floor, surface, balcony, price, forHowManyPeople);
    }
    public String getInfo() {
        return super.getInfo() + "This is a Family Room.";
    }
}
