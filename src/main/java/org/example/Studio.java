package org.example;
import jakarta.persistence.*;

@Entity
@Table(name = "studios")
public class Studio extends Room {

    Studio(long ID, int number, int floor, double surface, boolean balcony, double price, long version) {
        super(ID, number, floor, surface, balcony, price, version);
    }

    public Studio() {

    }


    @Override
    public String getInfo() {
        return super.getInfo() + " This is a Studio.";
    }
}
