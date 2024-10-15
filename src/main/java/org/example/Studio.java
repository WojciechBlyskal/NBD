package org.example;
import jakarta.persistence.*;

@Entity
@Table(name = "studios")
public class Studio extends Room {

    Studio(int number, int floor, double surface, boolean balcony, double price) {
        super(number, floor, surface, balcony, price);
    }

    public Studio() {

    }


    @Override
    public String getInfo() {
        return super.getInfo() + " This is a Studio.";
    }
}
