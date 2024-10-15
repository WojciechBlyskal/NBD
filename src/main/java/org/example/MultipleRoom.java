package org.example;

import org.example.exception.RoomException;
import jakarta.persistence.*;

@Entity
@Table(name = "multipleRooms")
public class MultipleRoom extends Room {
    @Column
    private int forHowManyPeople;

    MultipleRoom(int number, int floor, double surface, boolean balcony, double price, int forHowManyPeople, long version) {
        super(number, floor, surface, balcony, price, version);
        if (forHowManyPeople < 1) {
            throw new RoomException("Room cannot be rented to less than 1 person.");
        } else {
            this.forHowManyPeople = forHowManyPeople;
        }
    }

    public MultipleRoom() {

    }


    public int getForHowManyPeople() {
        return forHowManyPeople;
    }
    @Override
    public String getInfo() {
        return super.getInfo() + "For " + String.valueOf(getForHowManyPeople()) + " people.";
    }
}
