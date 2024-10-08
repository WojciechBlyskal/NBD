package org.example;

import org.example.exception.RoomException;

public abstract class MultipleRoom extends Room {
    private int forHowManyPeople;
    MultipleRoom(int number, int floor, double surface, boolean balcony, double price, int forHowManyPeople) {
        super(number, floor, surface, balcony, price);
        if (forHowManyPeople < 1) {
            throw new RoomException("Room cannot be rented to less than 1 person.");
        } else {
            this.forHowManyPeople = forHowManyPeople;
        }
    }

    public int getForHowManyPeople() {
        return forHowManyPeople;
    }
    @Override
    public String getInfo() {
        return super.getInfo() + "For " + String.valueOf(getForHowManyPeople()) + " people.";
    }
}
